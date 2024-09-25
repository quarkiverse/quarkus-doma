package io.quarkiverse.doma.deployment;

import java.util.Map;
import java.util.Optional;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;

import io.quarkiverse.doma.runtime.DomaSettings;
import io.quarkus.runtime.annotations.ConfigDocMapKey;
import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithParentName;

@ConfigMapping(prefix = "quarkus.doma")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface DomaBuildTimeConfig {

    String SQL_LOAD_SCRIPT_DEFAULT = "import.sql";
    String SQL_LOAD_SCRIPT_NO_FILE = "no-file";

    /** The default datasource. */
    @WithParentName
    DataSourceBuildTimeConfig defaultDataSource();

    /** Additional named datasources. */
    @ConfigDocSection
    @ConfigDocMapKey("datasource-name")
    @WithParentName
    Map<String, DataSourceBuildTimeConfig> namedDataSources();

    /**
     * The SQL file repository.
     *
     * @see Config#getSqlFileRepository()
     */
    @WithDefault("greedy-cache")
    DomaSettings.SqlFileRepositoryType sqlFileRepository();

    /**
     * The naming convention controller.
     *
     * @see Config#getNaming()
     */
    @WithDefault("none")
    DomaSettings.NamingType naming();

    /**
     * The SQL log type that determines the SQL log format in exceptions.
     *
     * @see Config#getExceptionSqlLogType()
     */
    @WithDefault("none")
    SqlLogType exceptionSqlLogType();

    interface DataSourceBuildTimeConfig {
        /**
         * The SQL dialect.
         *
         * @see Config#getDialect()
         */
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        Optional<DomaSettings.DialectType> dialect();

        /**
         * The batch size.
         *
         * @see Config#getBatchSize()
         */
        @WithDefault(DefaultDataSourceBuildTimeConfig.DEFAULT_BATCH_SIZE)
        int batchSize();

        /**
         * The fetch size.
         *
         * @see Config#getFetchSize()
         */
        @WithDefault(DefaultDataSourceBuildTimeConfig.DEFAULT_FETCH_SIZE)
        int fetchSize();

        /**
         * The max rows.
         *
         * @see Config#getMaxRows()
         */
        @WithDefault(DefaultDataSourceBuildTimeConfig.DEFAULT_MAX_ROWS)
        int maxRows();

        /**
         * The query timeout limit in seconds.
         *
         * @see Config#getQueryTimeout()
         */
        @WithDefault(DefaultDataSourceBuildTimeConfig.DEFAULT_QUERY_TIMEOUT)
        int queryTimeout();

        /**
         * Name of the file containing the SQL statements to execute when Doma starts. Its default value
         * differs depending on the Quarkus launch mode:
         *
         * <p>
         * * In dev and test modes, it defaults to `import.sql`. Simply add an `import.sql` file in
         * the root of your resources directory and it will be picked up without having to set this
         * property. Pass `no-file` to force Doma to ignore the SQL import file. * In production mode,
         * it defaults to `no-file`. It means Doma won't try to execute any SQL import file by default.
         * Pass an explicit value to force Doma to execute the SQL import file.
         */
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        Optional<String> sqlLoadScript();
    }
}
