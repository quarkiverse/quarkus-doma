package io.quarkiverse.doma.deployment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import io.quarkus.test.QuarkusUnitTest;

public class ScriptExecutorMultiFilesTest {

    @RegisterExtension
    static QuarkusUnitTest runner = new QuarkusUnitTest()
            .setArchiveProducer(
                    () -> ShrinkWrap.create(JavaArchive.class)
                            .add(
                                    new StringAsset(
                                            "quarkus.datasource.db-kind=h2\n"
                                                    + "quarkus.datasource.username=USERNAME-NAMED\n"
                                                    + "quarkus.datasource.jdbc.url=jdbc:h2:tcp://localhost/mem:testing_default\n"
                                                    + "quarkus.datasource.jdbc.driver=org.h2.Driver\n"
                                                    + "quarkus.datasource.sales.db-kind=h2\n"
                                                    + "quarkus.datasource.sales.username=USERNAME-NAMED\n"
                                                    + "quarkus.datasource.sales.jdbc.url=jdbc:h2:tcp://localhost/mem:testing_sales\n"
                                                    + "quarkus.datasource.sales.jdbc.driver=org.h2.Driver\n"
                                                    + "quarkus.doma.sql-load-script=import.sql\n"
                                                    + "quarkus.doma.sales.sql-load-script=import_sales.sql\n"
                                                    + "quarkus.doma.log.sql=true\n"),
                                    "application.properties")
                            .addAsResource("import.sql")
                            .addAsResource("import_sales.sql"));

    @Inject
    AgroalDataSource dataSource;

    @Inject
    @DataSource("sales")
    AgroalDataSource salesDataSource;

    @Test
    public void test() throws Exception {
        assertNotEquals(dataSource, salesDataSource);
        assertEquals(2, count(dataSource));
        assertEquals(2, count(salesDataSource));
    }

    private int count(AgroalDataSource ds) throws Exception {
        int count = 0;
        try (Connection connection = ds.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("select id from employee")) {
                    while (resultSet.next()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
