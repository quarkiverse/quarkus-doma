package io.quarkiverse.doma.deployment;

import static org.hamcrest.core.Is.is;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusDevModeTest;
import io.restassured.RestAssured;

public class HotReplacementTest {

    @RegisterExtension
    static final QuarkusDevModeTest runner = new QuarkusDevModeTest()
            .setArchiveProducer(
                    () -> ShrinkWrap.create(JavaArchive.class)
                            .add(
                                    new StringAsset(
                                            "quarkus.datasource.db-kind=h2\n"
                                                    + "quarkus.datasource.username=USERNAME-NAMED\n"
                                                    + "quarkus.datasource.jdbc.url=jdbc:h2:tcp://localhost/mem:testing\n"
                                                    + "quarkus.datasource.jdbc.driver=org.h2.Driver\n"
                                                    + "quarkus.http.port=8083"),
                                    "application.properties")
                            .addAsResource(HotReplacementResource.SQL_FILE)
                            .addAsResource(HotReplacementResource.SCRIPT_FILE)
                            .addClass(HotReplacementResource.class));

    @BeforeAll
    public static void setUpPort() {
        RestAssured.port = 8083;
    }

    @AfterAll
    public static void tearDownPort() {
        RestAssured.reset();
    }

    @Test
    public void sql() {
        RestAssured.when().get("/hot/sql").then().body(is("select\n*\nfrom\nemployee\n"));
        runner.modifyResourceFile(
                HotReplacementResource.SQL_FILE, s -> s.replaceAll("employee", "department"));
        RestAssured.when().get("/hot/sql").then().body(is("select\n*\nfrom\ndepartment\n"));
    }

    @Test
    public void script() {
        RestAssured.when().get("/hot/script").then().body(is("create table employee (\n  id int\n)\n"));
        runner.modifyResourceFile(
                HotReplacementResource.SCRIPT_FILE, s -> s.replaceAll("employee", "department"));
        RestAssured.when()
                .get("/hot/script")
                .then()
                .body(is("create table department (\n  id int\n)\n"));
    }
}
