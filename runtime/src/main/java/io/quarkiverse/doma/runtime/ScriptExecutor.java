package io.quarkiverse.doma.runtime;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.jboss.logging.Logger;
import org.seasar.doma.internal.util.ResourceUtil;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.runtime.DataSources;
import io.quarkus.arc.DefaultBean;
import io.quarkus.runtime.StartupEvent;

@Singleton
@DefaultBean
public class ScriptExecutor {

    private static final Logger logger = Logger.getLogger(ScriptExecutor.class.getName());

    private final Map<String, String> namedSqlLoadScripts;

    @Inject
    public ScriptExecutor(
            @Named("doma.namedSqlLoadScripts") Map<String, String> namedSqlLoadScripts) {
        Objects.requireNonNull(namedSqlLoadScripts);
        this.namedSqlLoadScripts = Collections.unmodifiableMap(namedSqlLoadScripts);
    }

    void onStartup(@Observes StartupEvent event) throws Exception {
        for (Map.Entry<String, String> entry : namedSqlLoadScripts.entrySet()) {
            String name = entry.getKey();
            String path = entry.getValue();
            AgroalDataSource dataSource = DataSources.fromName(name);
            if (dataSource == null) {
                throw new IllegalStateException(String.format("The datasource '%s' is not found.", name));
            }
            execute(dataSource, path);
        }
    }

    private void execute(DataSource dataSource, String path) throws Exception {
        logger.infof("Execute %s", path);
        String sql = ResourceUtil.getResourceAsString(path);
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }
        }
    }
}
