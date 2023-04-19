package io.quarkiverse.doma.deployment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.SqlLogType;

import io.quarkus.datasource.common.runtime.DataSourceUtil;
import io.quarkus.test.QuarkusUnitTest;

public class ApplicationPropertiesTest {

    @SuppressWarnings("unused")
    @RegisterExtension
    static QuarkusUnitTest runner = new QuarkusUnitTest()
            .setArchiveProducer(
                    () -> ShrinkWrap.create(JavaArchive.class)
                            .addClasses()
                            .add(
                                    new StringAsset(
                                            "quarkus.datasource.db-kind=h2\n"
                                                    + "quarkus.datasource.username=USERNAME-NAMED\n"
                                                    + "quarkus.datasource.jdbc.url=jdbc:h2:tcp://localhost/mem:testing\n"
                                                    + "quarkus.datasource.jdbc.driver=org.h2.Driver\n"
                                                    + "quarkus.doma.sql-file-repository=no-cache\n"
                                                    + "quarkus.doma.naming=upper-case\n"
                                                    + "quarkus.doma.exception-sql-log-type=raw\n"
                                                    + "quarkus.doma.dialect=postgres\n"
                                                    + "quarkus.doma.batch-size=10\n"
                                                    + "quarkus.doma.fetch-size=20\n"
                                                    + "quarkus.doma.max-rows=30\n"
                                                    + "quarkus.doma.query-timeout=40\n"
                                                    + "quarkus.doma.log.sql=true\n"
                                                    + "quarkus.doma.log.dao=true\n"
                                                    + "quarkus.doma.log.closing-failure=true\n"
                                                    + "quarkus.log.category.\"org.seasar.doma\".level=DEBUG\n"),
                                    "application.properties"));

    @Inject
    Config config;

    @Test
    public void test() {
        assertNotNull(config);
        assertEquals("postgres", config.getDialect().getName());
        assertTrue(config.getSqlFileRepository().toString().contains("NoCacheSqlFileRepository"));
        assertEquals(Naming.UPPER_CASE, config.getNaming());
        assertEquals(SqlLogType.RAW, config.getExceptionSqlLogType());
        assertEquals(DataSourceUtil.DEFAULT_DATASOURCE_NAME, config.getDataSourceName());
        assertEquals(10, config.getBatchSize());
        assertEquals(20, config.getFetchSize());
        assertEquals(30, config.getMaxRows());
        assertEquals(40, config.getQueryTimeout());
    }
}
