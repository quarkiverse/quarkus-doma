package io.quarkiverse.doma.runtime;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.CommandImplementors;
import org.seasar.doma.jdbc.Commenter;
import org.seasar.doma.jdbc.ConfigSupport;
import org.seasar.doma.jdbc.DuplicateColumnHandler;
import org.seasar.doma.jdbc.EntityListenerProvider;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.MapKeyNaming;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.QueryImplementors;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.ScriptFileLoader;
import org.seasar.doma.jdbc.SqlBuilderSettings;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.statistic.StatisticManager;
import org.seasar.doma.jdbc.tx.TransactionManager;
import org.seasar.doma.slf4j.Slf4jJdbcLogger;

import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.Unremovable;

@SuppressWarnings({ "SameReturnValue", "unused" })
@Singleton
public class DomaProducer {

    private volatile SqlFileRepository sqlFileRepository;
    private volatile ScriptFileLoader scriptFileLoader;
    private volatile Naming naming;
    private volatile SqlLogType exceptionSqlLogType;
    private volatile Map<String, String> namedSqlLoadScripts;
    private volatile SqlBuilderSettings sqlBuilderSettings;
    private volatile DuplicateColumnHandler duplicateColumnHandler;

    public void setSqlFileRepository(SqlFileRepository sqlFileRepository) {
        this.sqlFileRepository = Objects.requireNonNull(sqlFileRepository);
    }

    public void setScriptFileLoader(ScriptFileLoader scriptFileLoader) {
        this.scriptFileLoader = Objects.requireNonNull(scriptFileLoader);
    }

    public void setNaming(Naming naming) {
        this.naming = Objects.requireNonNull(naming);
    }

    public void setExceptionSqlLogType(SqlLogType exceptionSqlLogType) {
        this.exceptionSqlLogType = Objects.requireNonNull(exceptionSqlLogType);
    }

    public void setNamedSqlLoadScripts(Map<String, String> namedSqlLoadScripts) {
        Objects.requireNonNull(namedSqlLoadScripts);
        this.namedSqlLoadScripts = Collections.unmodifiableMap(namedSqlLoadScripts);
    }

    public void setSqlBuilderSettings(SqlBuilderSettings sqlBuilderSettings) {
        this.sqlBuilderSettings = Objects.requireNonNull(sqlBuilderSettings);
    }

    public void setDuplicateColumnHandler(DuplicateColumnHandler duplicateColumnHandler) {
        this.duplicateColumnHandler = duplicateColumnHandler;
    }

    @Singleton
    @DefaultBean
    SqlFileRepository sqlFileRepository() {
        return Objects.requireNonNull(sqlFileRepository);
    }

    @Singleton
    @DefaultBean
    ScriptFileLoader scriptFileLoader() {
        return Objects.requireNonNull(scriptFileLoader);
    }

    @Singleton
    @DefaultBean
    SqlBuilderSettings sqlBuilderSettings() {
        return Objects.requireNonNull(sqlBuilderSettings);
    }

    @Singleton
    @DefaultBean
    Slf4jJdbcLogger jdbcLogger() {
        return new Slf4jJdbcLogger();
    }

    @Singleton
    @DefaultBean
    ClassHelper classHelper() {
        return ConfigSupport.defaultClassHelper;
    }

    @Singleton
    @DefaultBean
    CommandImplementors commandImplementors() {
        return ConfigSupport.defaultCommandImplementors;
    }

    @Singleton
    @DefaultBean
    QueryImplementors queryImplementors() {
        return ConfigSupport.defaultQueryImplementors;
    }

    @Singleton
    @DefaultBean
    UnknownColumnHandler unknownColumnHandler() {
        return ConfigSupport.defaultUnknownColumnHandler;
    }

    @Singleton
    @DefaultBean
    Naming naming() {
        return Objects.requireNonNull(naming);
    }

    @Singleton
    @DefaultBean
    MapKeyNaming mapKeyNaming() {
        return ConfigSupport.defaultMapKeyNaming;
    }

    @Singleton
    @DefaultBean
    Commenter commenter() {
        return ConfigSupport.defaultCommenter;
    }

    @Singleton
    @DefaultBean
    DuplicateColumnHandler duplicateColumnHandler() {
        return duplicateColumnHandler;
    }

    @Singleton
    @DefaultBean
    StatisticManager statisticManager() {
        return ConfigSupport.defaultStatisticManager;
    }

    @Singleton
    @DefaultBean
    EntityListenerProvider entityListenerProvider() {
        return ConfigSupport.defaultEntityListenerProvider;
    }

    @Singleton
    @Named("doma.exceptionSqlLogType")
    SqlLogType exceptionSqlLogType() {
        return Objects.requireNonNull(exceptionSqlLogType);
    }

    @Singleton
    @Named("doma.namedSqlLoadScripts")
    public Map<String, String> getNamedSqlLoadScripts() {
        return namedSqlLoadScripts;
    }

    @Singleton
    @DefaultBean
    @Unremovable
    DomaConfig.Core core(
            SqlFileRepository sqlFileRepository,
            ScriptFileLoader scriptFileLoader,
            JdbcLogger jdbcLogger,
            RequiresNewController requiresNewController,
            ClassHelper classHelper,
            CommandImplementors commandImplementors,
            QueryImplementors queryImplementors,
            @Named("doma.exceptionSqlLogType") SqlLogType exceptionSqlLogType,
            UnknownColumnHandler unknownColumnHandler,
            Naming naming,
            MapKeyNaming mapKeyNaming,
            Commenter commenter,
            EntityListenerProvider entityListenerProvider,
            TransactionManager transactionManager,
            DuplicateColumnHandler duplicateColumnHandler,
            SqlBuilderSettings sqlBuilderSettings,
            StatisticManager statisticManager) {
        return new DomaConfig.Core(
                sqlFileRepository,
                scriptFileLoader,
                jdbcLogger,
                requiresNewController,
                classHelper,
                commandImplementors,
                queryImplementors,
                exceptionSqlLogType,
                unknownColumnHandler,
                naming,
                mapKeyNaming,
                commenter,
                entityListenerProvider,
                transactionManager,
                duplicateColumnHandler,
                sqlBuilderSettings,
                statisticManager);
    }
}
