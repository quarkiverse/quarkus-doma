package io.quarkiverse.doma.deployment;

import static org.hamcrest.core.Is.is;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.doma.runtime.DataSourceNameResolver;
import io.quarkus.arc.Unremovable;
import io.quarkus.datasource.common.runtime.DataSourceUtil;
import io.quarkus.test.QuarkusUnitTest;
import io.restassured.RestAssured;
import io.vertx.ext.web.RoutingContext;

public class DataSourceResolverTest {

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
                            .addAsResource("import_sales.sql")
                            .addClass(DataSourceResolverResource.class)
                            .addClass(TenantResolver.class));

    @RequestScoped
    @Unremovable
    static class TenantResolver implements DataSourceNameResolver {

        @Inject
        RoutingContext context;

        @Override
        public String resolve(String candidateName) {
            String path = context.request().path();
            String[] parts = path.split("/");
            if (parts.length == 0) {
                throw new IllegalStateException("parts.length == 0");
            }
            String dataSourceName = parts[1];
            if (dataSourceName == null || dataSourceName.equals("default")) {
                return DataSourceUtil.DEFAULT_DATASOURCE_NAME;
            }
            return dataSourceName;
        }
    }

    @Test
    void test() {
        RestAssured.when().get("/default/1").then().body(is("aaa"));
        RestAssured.when().get("/sales/1").then().body(is("aaa_sales"));
    }
}
