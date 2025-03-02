package io.quarkiverse.doma.deployment;

import java.util.Map;
import java.util.Optional;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlBuilderSettings;
import org.seasar.doma.jdbc.SqlLogType;

import io.quarkiverse.doma.runtime.DomaSettings;
import io.quarkus.datasource.common.runtime.DataSourceUtil;
import io.quarkus.runtime.annotations.ConfigDocDefault;
import io.quarkus.runtime.annotations.ConfigDocMapKey;
import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithDefaults;
import io.smallrye.config.WithParentName;
import io.smallrye.config.WithUnnamedKey;

@ConfigRoot
@ConfigMapping(prefix = "quarkus.doma")
public interface DomaBuildTimeConfig {

    public static final String SQL_LOAD_SCRIPT_DEFAULT = "import.sql";
    public static final String SQL_LOAD_SCRIPT_NO_FILE = "no-file";

    /** Datasources. */
    @ConfigDocSection
    @ConfigDocMapKey("datasource-name")
    @WithParentName
    @WithUnnamedKey(DataSourceUtil.DEFAULT_DATASOURCE_NAME)
    @WithDefaults
    Map<String, DataSourceBuildTimeConfig> dataSources();

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

    SqlBuilderSettingsBuildTimeConfig sqlBuilderSettings();

    /**
     * An exception will be thrown if duplicate columns exist.
     * If {@link org.seasar.doma.jdbc.DuplicateColumnHandler} is registered in CDI, this property will be ignored.
     *
     * @see Config#getDuplicateColumnHandler()
     */
    @WithDefault("false")
    boolean throwExceptionIfDuplicateColumn();

    @ConfigGroup
    public interface DataSourceBuildTimeConfig {
        /**
         * The SQL dialect.
         *
         * @see Config#getDialect()
         */
        @ConfigDocDefault("depends on 'quarkus.datasource.db-kind'")
        Optional<DomaSettings.DialectType> dialect();

        /**
         * The batch size.
         *
         * @see Config#getBatchSize()
         */
        @WithDefault("0")
        int batchSize();

        /**
         * The fetch size.
         *
         * @see Config#getFetchSize()
         */
        @WithDefault("0")
        int fetchSize();

        /**
         * The max rows.
         *
         * @see Config#getMaxRows()
         */
        @WithDefault("0")
        int maxRows();

        /**
         * The query timeout limit in seconds.
         *
         * @see Config#getQueryTimeout()
         */
        @WithDefault("0")
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
        @ConfigDocDefault("import.sql in DEV, TEST ; no-file otherwise")
        Optional<String> sqlLoadScript();
    }

    @ConfigGroup
    public interface SqlBuilderSettingsBuildTimeConfig {
        /**
         * Whether to remove blank lines from SQL.
         * If {@link org.seasar.doma.jdbc.SqlBuilderSettings} is registered in CDI, this property will be ignored.
         *
         * @see SqlBuilderSettings#shouldRemoveBlankLines()
         */
        @WithDefault("false")
        boolean shouldRemoveBlankLines();

        /**
         * Whether to enable IN list padding.
         * If {@link org.seasar.doma.jdbc.SqlBuilderSettings} is registered in CDI, this property will be ignored.
         *
         * @see SqlBuilderSettings#shouldRequireInListPadding()
         */
        @WithDefault("false")
        boolean shouldRequireInListPadding();
    }
}
