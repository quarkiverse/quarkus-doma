package io.quarkiverse.doma.deployment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.NativeSql;

import io.agroal.api.AgroalDataSource;
import io.quarkiverse.doma.runtime.DomaConfig;
import io.quarkus.agroal.DataSource;
import io.quarkus.test.QuarkusUnitTest;
import org.seasar.doma.jdbc.criteria.QueryDsl;

public class QualifierTest {

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
                                                    + "quarkus.datasource.inventory.db-kind=h2\n"
                                                    + "quarkus.datasource.inventory.username=USERNAME-NAMED\n"
                                                    + "quarkus.datasource.inventory.jdbc.url=jdbc:h2:tcp://localhost/mem:testing\n"
                                                    + "quarkus.datasource.inventory.jdbc.driver=org.h2.Driver\n"),
                                    "application.properties"));

    @Inject
    DomaConfig config;

    @Inject
    @DataSource("inventory")
    Config inventoryConfig;

    @Inject
    AgroalDataSource dataSource;

    @Inject
    @DataSource("inventory")
    AgroalDataSource inventoryDataSource;

    @Inject
    Entityql entityql;

    @Inject
    @DataSource("inventory")
    Entityql inventoryEntityql;

    @Inject
    NativeSql nativeSql;

    @Inject
    @DataSource("inventory")
    NativeSql inventoryNativeSql;

    @Inject
    QueryDsl queryDsl;

    @Inject
    @DataSource("inventory")
    QueryDsl inventoryQueryDsl;

    @Test
    void testDataSource() {
        assertEquals(dataSource.toString(), config.getDataSource().toString());
        assertEquals(inventoryDataSource.toString(), inventoryConfig.getDataSource().toString());
    }

    @Test
    void testEntityql() {
        assertNotNull(entityql);
        assertNotNull(inventoryEntityql);
    }

    @Test
    void testNativeSql() {
        assertNotNull(nativeSql);
        assertNotNull(inventoryNativeSql);
    }

    @Test
    void testQueryDsl() {
        assertNotNull(queryDsl);
        assertNotNull(inventoryQueryDsl);
    }
}
