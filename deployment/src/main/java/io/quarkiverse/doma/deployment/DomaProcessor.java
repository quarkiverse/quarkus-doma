package io.quarkiverse.doma.deployment;

import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.enterprise.inject.Default;
import javax.inject.Singleton;

import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.seasar.doma.DaoImplementation;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.NativeSql;

import io.quarkiverse.doma.runtime.DataSourceNameResolver;
import io.quarkiverse.doma.runtime.DataSourceResolver;
import io.quarkiverse.doma.runtime.DomaConfig;
import io.quarkiverse.doma.runtime.DomaProducer;
import io.quarkiverse.doma.runtime.DomaRecorder;
import io.quarkiverse.doma.runtime.DomaSettings;
import io.quarkiverse.doma.runtime.JtaRequiresNewController;
import io.quarkiverse.doma.runtime.ScriptExecutor;
import io.quarkiverse.doma.runtime.UnsupportedTransactionManager;
import io.quarkus.agroal.DataSource;
import io.quarkus.agroal.spi.JdbcDataSourceBuildItem;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanArchiveIndexBuildItem;
import io.quarkus.arc.deployment.BeanContainerListenerBuildItem;
import io.quarkus.arc.deployment.BeanDefiningAnnotationBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ApplicationArchivesBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.HotDeploymentWatchedFileBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

class DomaProcessor {

    private static final String FEATURE = "doma";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem additionalBeans() {
        return new AdditionalBeanBuildItem(
                DomaProducer.class,
                JtaRequiresNewController.class,
                ScriptExecutor.class,
                UnsupportedTransactionManager.class,
                DataSourceNameResolver.DefaultDataSourceNameResolver.class,
                DataSourceResolver.DefaultDataSourceResolver.class);
    }

    @BuildStep
    BeanDefiningAnnotationBuildItem beanDefiningAnnotation() {
        return new BeanDefiningAnnotationBuildItem(
                DotName.createSimple(DaoImplementation.class.getName()));
    }

    @BuildStep
    DomaSettingsBuildItem domaSettings(
            DomaBuildTimeConfig buildTimeConfig,
            List<JdbcDataSourceBuildItem> dataSources,
            ApplicationArchivesBuildItem applicationArchives,
            LaunchModeBuildItem launchMode) {
        DomaSettingsFactory factory = new DomaSettingsFactory(buildTimeConfig, dataSources, applicationArchives, launchMode);
        return new DomaSettingsBuildItem(factory.create());
    }

    @BuildStep
    List<HotDeploymentWatchedFileBuildItem> hotDeploymentWatchedFile(DomaSettingsBuildItem settings) {
        return settings.getSettings().dataSources.stream()
                .map(it -> it.sqlLoadScript)
                .filter(Objects::nonNull)
                .map(HotDeploymentWatchedFileBuildItem::new)
                .collect(toList());
    }

    @BuildStep
    NativeImageResourceBuildItem nativeImageResources(DomaSettingsBuildItem settings) {
        List<String> resources = new ArrayList<>();
        settings.getSettings().dataSources.stream()
                .map(it -> it.sqlLoadScript)
                .filter(Objects::nonNull)
                .forEach(resources::add);
        DomaResourceScanner scanner = new DomaResourceScanner();
        List<String> scannedResources = scanner.scan();
        resources.addAll(scannedResources);
        return new NativeImageResourceBuildItem(resources);
    }

    @BuildStep
    ReflectiveClassBuildItem reflectiveClasses(BeanArchiveIndexBuildItem beanArchiveIndex) {
        List<String> classes = new ArrayList<>();
        classes.add(ScriptExecutor.class.getName());
        IndexView indexView = beanArchiveIndex.getIndex();
        DomaClassScanner scanner = new DomaClassScanner(indexView);
        List<String> scannedClasses = scanner.scan();
        classes.addAll(scannedClasses);
        return new ReflectiveClassBuildItem(true, true, classes.toArray(new String[0]));
    }

    @BuildStep
    @Record(STATIC_INIT)
    void registerBeans(
            DomaRecorder recorder,
            LaunchModeBuildItem launchMode,
            DomaSettingsBuildItem settings,
            BuildProducer<BeanContainerListenerBuildItem> beanContainerListeners,
            BuildProducer<SyntheticBeanBuildItem> syntheticBeans) {

        DomaSettings domaSettings = settings.getSettings();

        beanContainerListeners.produce(
                new BeanContainerListenerBuildItem(
                        recorder.configure(domaSettings, launchMode.getLaunchMode())));

        registerSyntheticBeans(
                domaSettings.dataSources,
                syntheticBeans,
                DomaConfig.class,
                Config.class,
                recorder::configureConfig);

        registerSyntheticBeans(
                domaSettings.dataSources,
                syntheticBeans,
                Entityql.class,
                Entityql.class,
                recorder::configureEntityql);

        registerSyntheticBeans(
                domaSettings.dataSources,
                syntheticBeans,
                NativeSql.class,
                NativeSql.class,
                recorder::configureNativeSql);
    }

    private <BEAN> void registerSyntheticBeans(
            List<DomaSettings.DataSourceSettings> dataSourceSettingsList,
            BuildProducer<SyntheticBeanBuildItem> syntheticBeans,
            Class<? extends BEAN> implClazz,
            Class<BEAN> typeClazz,
            Function<DomaSettings.DataSourceSettings, Supplier<BEAN>> supplierCreator) {
        dataSourceSettingsList.stream()
                .map(
                        dataSourceSettings -> {
                            SyntheticBeanBuildItem.ExtendedBeanConfigurator configurator = SyntheticBeanBuildItem
                                    .configure(implClazz)
                                    .addType(DotName.createSimple(typeClazz.getName()))
                                    .scope(Singleton.class)
                                    .unremovable()
                                    .supplier(supplierCreator.apply(dataSourceSettings));
                            if (dataSourceSettings.isDefault) {
                                configurator.addQualifier().annotation(Default.class).done();
                            } else {
                                configurator
                                        .addQualifier()
                                        .annotation(DataSource.class)
                                        .addValue("value", dataSourceSettings.name)
                                        .done();
                            }
                            return configurator.done();
                        })
                .forEach(syntheticBeans::produce);
    }
}
