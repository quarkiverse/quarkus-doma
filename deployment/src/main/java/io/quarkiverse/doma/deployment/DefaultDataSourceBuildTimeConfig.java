package io.quarkiverse.doma.deployment;

import java.util.Optional;

import io.quarkiverse.doma.runtime.DomaSettings;

public class DefaultDataSourceBuildTimeConfig implements DomaBuildTimeConfig.DataSourceBuildTimeConfig {
    public static final String DEFAULT_BATCH_SIZE = "0";
    public static final String DEFAULT_FETCH_SIZE = "0";
    public static final String DEFAULT_MAX_ROWS = "0";
    public static final String DEFAULT_QUERY_TIMEOUT = "0";

    @Override
    public Optional<DomaSettings.DialectType> dialect() {
        return Optional.empty();
    }

    @Override
    public int batchSize() {
        return Integer.parseInt(DEFAULT_BATCH_SIZE);
    }

    @Override
    public int fetchSize() {
        return Integer.parseInt(DEFAULT_FETCH_SIZE);
    }

    @Override
    public int maxRows() {
        return Integer.parseInt(DEFAULT_MAX_ROWS);
    }

    @Override
    public int queryTimeout() {
        return Integer.parseInt(DEFAULT_QUERY_TIMEOUT);
    }

    @Override
    public Optional<String> sqlLoadScript() {
        return Optional.empty();
    }
}
