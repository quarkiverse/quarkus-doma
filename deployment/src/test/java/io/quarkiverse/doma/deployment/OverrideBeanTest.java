package io.quarkiverse.doma.deployment;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.doma.jdbc.CommentContext;
import org.seasar.doma.jdbc.Commenter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.NoCacheSqlFileRepository;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;
import org.seasar.doma.jdbc.tx.TransactionManager;

import io.quarkus.arc.Unremovable;
import io.quarkus.test.QuarkusUnitTest;

public class OverrideBeanTest {

    @RegisterExtension
    static QuarkusUnitTest runner = new QuarkusUnitTest()
            .setArchiveProducer(
                    () -> ShrinkWrap.create(JavaArchive.class)
                            .add(
                                    new StringAsset(
                                            "quarkus.datasource.db-kind=h2\n"
                                                    + "quarkus.datasource.username=USERNAME-NAMED\n"
                                                    + "quarkus.datasource.jdbc.url=jdbc:h2:tcp://localhost/mem:testing\n"
                                                    + "quarkus.datasource.jdbc.driver=org.h2.Driver\n"),
                                    "application.properties")
                            .addClasses(MyProducer.class, MyCommenter.class));

    @Inject
    Config config;

    static class MyProducer {

        @ApplicationScoped
        SqlFileRepository sqlFileRepository() {
            return new NoCacheSqlFileRepository();
        }

        @ApplicationScoped
        TransactionManager transactionManager(DataSource dataSource, JdbcLogger jdbcLogger) {
            LocalTransactionDataSource localTransactionDataSource = new LocalTransactionDataSource(dataSource);
            return new LocalTransactionManager(
                    localTransactionDataSource.getLocalTransaction(jdbcLogger));
        }
    }

    // @Inject MyCommenter c;

    @ApplicationScoped
    @Unremovable
    static class MyCommenter implements Commenter {
        @Override
        public String comment(String sql, CommentContext context) {
            return "hello";
        }
    }

    @Test
    void test() {
        //
        // CDI.current().getBeanManager().createInstance().select(Commenter.class).iterator().forEachRemaining(System.out::println);
        // System.out.println(c2);
        //    assertTrue(config.getSqlFileRepository().toString().contains("NoCacheSqlFileRepository"));
        assertTrue(config.getCommenter() instanceof MyCommenter);
        assertNotNull(config.getTransactionManager());
    }
}
