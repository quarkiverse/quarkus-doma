package io.quarkiverse.doma.deployment;

import java.util.Map;
import java.util.Optional;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlBuilderSettings;
import org.seasar.doma.jdbc.SqlLogType;

import io.quarkiverse.doma.runtime.DomaSettings;
import io.quarkus.runtime.annotations.ConfigDocMapKey;
import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot
public class DomaBuildTimeConfig {

    public static final String SQL_LOAD_SCRIPT_DEFAULT = "import.sql";
    public static final String SQL_LOAD_SCRIPT_NO_FILE = "no-file";

    /** The default datasource. */
    @ConfigItem(name = ConfigItem.PARENT)
    public DataSourceBuildTimeConfig defaultDataSource;

    /** Additional named datasources. */
    @ConfigDocSection
    @ConfigDocMapKey("datasource-name")
    @ConfigItem(name = ConfigItem.PARENT)
    public Map<String, DataSourceBuildTimeConfig> namedDataSources;

    /**
     * The SQL file repository.
     *
     * @see Config#getSqlFileRepository()
     */
    @ConfigItem(defaultValue = "greedy-cache")
    public DomaSettings.SqlFileRepositoryType sqlFileRepository;

    /**
     * The naming convention controller.
     *
     * @see Config#getNaming()
     */
    @ConfigItem(defaultValue = "none")
    public DomaSettings.NamingType naming;

    /**
     * The SQL log type that determines the SQL log format in exceptions.
     *
     * @see Config#getExceptionSqlLogType()
     */
    @ConfigItem(defaultValue = "none")
    public SqlLogType exceptionSqlLogType;

    public SqlBuilderBuildTimeConfig sqlBuilderSettings;

    /**
     * An exception will be thrown if duplicate columns exist.
     * If {@link org.seasar.doma.jdbc.DuplicateColumnHandler} is registered in CDI, this property will be ignored.
     *
     * @see Config#getDuplicateColumnHandler()
     */
    @ConfigItem(defaultValue = "false")
    public boolean throwExceptionIfDuplicateColumn;

    @SuppressWarnings("CanBeFinal")
    @ConfigGroup
    public static class DataSourceBuildTimeConfig {
        /**
         * The SQL dialect.
         *
         * @see Config#getDialect()
         */
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        @ConfigItem(defaultValueDocumentation = "depends on 'quarkus.datasource.db-kind'")
        public Optional<DomaSettings.DialectType> dialect = Optional.empty();

        /**
         * The batch size.
         *
         * @see Config#getBatchSize()
         */
        @ConfigItem(defaultValue = "0")
        public int batchSize = 0;

        /**
         * The fetch size.
         *
         * @see Config#getFetchSize()
         */
        @ConfigItem(defaultValue = "0")
        public int fetchSize = 0;

        /**
         * The max rows.
         *
         * @see Config#getMaxRows()
         */
        @ConfigItem(defaultValue = "0")
        public int maxRows = 0;

        /**
         * The query timeout limit in seconds.
         *
         * @see Config#getQueryTimeout()
         */
        @ConfigItem(defaultValue = "0")
        public int queryTimeout = 0;

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
        @ConfigItem(defaultValueDocumentation = "import.sql in DEV, TEST ; no-file otherwise")
        public Optional<String> sqlLoadScript = Optional.empty();

        @Override
        public String toString() {
            return "DataSourceBuildTimeConfig{"
                    + "dialect="
                    + dialect
                    + ", batchSize="
                    + batchSize
                    + ", fetchSize="
                    + fetchSize
                    + ", maxRows="
                    + maxRows
                    + ", queryTimeout="
                    + queryTimeout
                    + ", sqlLoadScript="
                    + sqlLoadScript
                    + '}';
        }
    }

    @SuppressWarnings("CanBeFinal")
    @ConfigGroup
    public static class SqlBuilderBuildTimeConfig {
        /**
         * Whether to remove blank lines from SQL.
         * If {@link org.seasar.doma.jdbc.SqlBuilderSettings} is registered in CDI, this property will be ignored.
         *
         * @see SqlBuilderSettings#shouldRemoveBlankLines()
         */
        @ConfigItem(defaultValue = "false")
        public boolean shouldRemoveBlankLines;
        /**
         * Whether to enable IN list padding.
         * If {@link org.seasar.doma.jdbc.SqlBuilderSettings} is registered in CDI, this property will be ignored.
         *
         * @see SqlBuilderSettings#shouldRequireInListPadding()
         */
        @ConfigItem(defaultValue = "false")
        public boolean shouldRequireInListPadding;

        @Override
        public String toString() {
            return "SqlBuilderBuildTimeConfig{"
                    + "shouldRemoveBlankLines="
                    + shouldRemoveBlankLines
                    + ", shouldRequireInListPadding="
                    + shouldRequireInListPadding
                    + '}';
        }
    }

    @Override
    public String toString() {
        return "DomaBuildTimeConfig{"
                + "defaultDataSource="
                + defaultDataSource
                + ", namedDataSources="
                + namedDataSources
                + ", sqlFileRepository="
                + sqlFileRepository
                + ", naming="
                + naming
                + ", exceptionSqlLogType="
                + exceptionSqlLogType
                + ", sqlBuilderSettings="
                + sqlBuilderSettings
                + ", throwExceptionIfDuplicateColumn="
                + throwExceptionIfDuplicateColumn
                + '}';
    }
}
