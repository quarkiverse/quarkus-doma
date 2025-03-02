package io.quarkiverse.doma.deployment;

import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.doma.jdbc.Config;

import io.quarkus.test.QuarkusUnitTest;

public class SqlBuilderSettingsTest {
    @SuppressWarnings("unused")
    @RegisterExtension
    static QuarkusUnitTest runner = new QuarkusUnitTest()
            .setArchiveProducer(
                    () -> ShrinkWrap.create(JavaArchive.class)
                            .add(
                                    new StringAsset(
                                            "quarkus.datasource.db-kind=h2\n"
                                                    + "quarkus.datasource.username=USERNAME-NAMED\n"
                                                    + "quarkus.datasource.jdbc.url=jdbc:h2:tcp://localhost/mem:testing\n"
                                                    + "quarkus.datasource.jdbc.driver=org.h2.Driver\n"
                                                    + "quarkus.doma.sql-builder-settings.should-require-in-list-padding=true\n"
                                                    + "quarkus.doma.sql-builder-settings.should-remove-blank-lines=true\n"),
                                    "application.properties"));

    @Inject
    Config config;

    @Test
    void testSqlBuilderSettings() {
        var sqlBuilderSettings = config.getSqlBuilderSettings();
        assertTrue(sqlBuilderSettings.shouldRequireInListPadding());
        assertTrue(sqlBuilderSettings.shouldRemoveBlankLines());
    }
}
