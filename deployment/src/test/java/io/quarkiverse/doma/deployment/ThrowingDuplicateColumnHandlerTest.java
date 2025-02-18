package io.quarkiverse.doma.deployment;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import jakarta.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ThrowingDuplicateColumnHandler;

import io.quarkus.test.QuarkusUnitTest;

public class ThrowingDuplicateColumnHandlerTest {
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
                                                    + "quarkus.doma.throw-exception-if-duplicate-column=true\n"),
                                    "application.properties"));

    @Inject
    Config config;

    @Test
    void testThrowingDuplicateColumn() {
        assertInstanceOf(ThrowingDuplicateColumnHandler.class, config.getDuplicateColumnHandler());
    }
}
