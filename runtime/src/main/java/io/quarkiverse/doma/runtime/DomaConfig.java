package io.quarkiverse.doma.runtime;

import java.util.Objects;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.CommandImplementors;
import org.seasar.doma.jdbc.Commenter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.EntityListenerProvider;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.MapKeyNaming;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.QueryImplementors;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.ScriptFileLoader;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.tx.TransactionManager;

public class DomaConfig implements Config {

    private final Supplier<String> dataSourceNameSupplier;
    private final DataSourceResolver dataSourceResolver;
    private final Dialect dialect;
    private final SqlFileRepository sqlFileRepository;
    private final ScriptFileLoader scriptFileLoader;
    private final JdbcLogger jdbcLogger;
    private final RequiresNewController requiresNewController;
    private final ClassHelper classHelper;
    private final CommandImplementors commandImplementors;
    private final QueryImplementors queryImplementors;
    private final SqlLogType exceptionSqlLogType;
    private final UnknownColumnHandler unknownColumnHandler;
    private final Naming naming;
    private final MapKeyNaming mapKeyNaming;
    private final Commenter commenter;
    private final EntityListenerProvider entityListenerProvider;
    private final TransactionManager transactionManager;
    private final int batchSize;
    private final int fetchSize;
    private final int maxRows;
    private final int queryTimeout;

    public DomaConfig(
            Supplier<String> dataSourceNameSupplier,
            DataSourceResolver dataSourceResolver,
            Dialect dialect,
            SqlFileRepository sqlFileRepository,
            ScriptFileLoader scriptFileLoader,
            JdbcLogger jdbcLogger,
            RequiresNewController requiresNewController,
            ClassHelper classHelper,
            CommandImplementors commandImplementors,
            QueryImplementors queryImplementors,
            SqlLogType exceptionSqlLogType,
            UnknownColumnHandler unknownColumnHandler,
            Naming naming,
            MapKeyNaming mapKeyNaming,
            Commenter commenter,
            EntityListenerProvider entityListenerProvider,
            TransactionManager transactionManager,
            int batchSize,
            int fetchSize,
            int maxRows,
            int queryTimeout) {
        this.dataSourceNameSupplier = Objects.requireNonNull(dataSourceNameSupplier);
        this.dataSourceResolver = Objects.requireNonNull(dataSourceResolver);
        this.dialect = Objects.requireNonNull(dialect);
        this.sqlFileRepository = Objects.requireNonNull(sqlFileRepository);
        this.scriptFileLoader = Objects.requireNonNull(scriptFileLoader);
        this.jdbcLogger = Objects.requireNonNull(jdbcLogger);
        this.requiresNewController = Objects.requireNonNull(requiresNewController);
        this.classHelper = Objects.requireNonNull(classHelper);
        this.commandImplementors = Objects.requireNonNull(commandImplementors);
        this.queryImplementors = Objects.requireNonNull(queryImplementors);
        this.exceptionSqlLogType = Objects.requireNonNull(exceptionSqlLogType);
        this.unknownColumnHandler = Objects.requireNonNull(unknownColumnHandler);
        this.naming = Objects.requireNonNull(naming);
        this.mapKeyNaming = Objects.requireNonNull(mapKeyNaming);
        this.commenter = Objects.requireNonNull(commenter);
        this.entityListenerProvider = Objects.requireNonNull(entityListenerProvider);
        this.transactionManager = Objects.requireNonNull(transactionManager);
        this.batchSize = batchSize;
        this.fetchSize = fetchSize;
        this.maxRows = maxRows;
        this.queryTimeout = queryTimeout;
    }

    public Supplier<String> getDataSourceNameSupplier() {
        return dataSourceNameSupplier;
    }

    public DataSourceResolver getDataSourceResolver() {
        return dataSourceResolver;
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public DataSource getDataSource() {
        return dataSourceResolver.resolve(dataSourceNameSupplier.get());
    }

    @Override
    public SqlFileRepository getSqlFileRepository() {
        return sqlFileRepository;
    }

    @Override
    public ScriptFileLoader getScriptFileLoader() {
        return scriptFileLoader;
    }

    @Override
    public JdbcLogger getJdbcLogger() {
        return jdbcLogger;
    }

    @Override
    public RequiresNewController getRequiresNewController() {
        return requiresNewController;
    }

    @Override
    public ClassHelper getClassHelper() {
        return classHelper;
    }

    @Override
    public CommandImplementors getCommandImplementors() {
        return commandImplementors;
    }

    @Override
    public QueryImplementors getQueryImplementors() {
        return queryImplementors;
    }

    @Override
    public SqlLogType getExceptionSqlLogType() {
        return exceptionSqlLogType;
    }

    @Override
    public UnknownColumnHandler getUnknownColumnHandler() {
        return unknownColumnHandler;
    }

    @Override
    public Naming getNaming() {
        return naming;
    }

    @Override
    public MapKeyNaming getMapKeyNaming() {
        return mapKeyNaming;
    }

    @Override
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    public Commenter getCommenter() {
        return commenter;
    }

    @Override
    public EntityListenerProvider getEntityListenerProvider() {
        return entityListenerProvider;
    }

    @Override
    public String getDataSourceName() {
        return dataSourceNameSupplier.get();
    }

    @Override
    public int getMaxRows() {
        return maxRows;
    }

    @Override
    public int getFetchSize() {
        return fetchSize;
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public int getQueryTimeout() {
        return queryTimeout;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(DomaConfig config) {
        Objects.requireNonNull(config);
        Builder builder = new Builder();
        builder.setDataSourceNameSupplier(config.getDataSourceNameSupplier());
        builder.setDataSourceResolver(config.getDataSourceResolver());
        builder.setDialect(config.getDialect());
        builder.setSqlFileRepository(config.getSqlFileRepository());
        builder.setScriptFileLoader(config.getScriptFileLoader());
        builder.setJdbcLogger(config.getJdbcLogger());
        builder.setRequiresNewController(config.getRequiresNewController());
        builder.setClassHelper(config.getClassHelper());
        builder.setCommandImplementors(config.getCommandImplementors());
        builder.setQueryImplementors(config.getQueryImplementors());
        builder.setExceptionSqlLogType(config.getExceptionSqlLogType());
        builder.setUnknownColumnHandler(config.getUnknownColumnHandler());
        builder.setNaming(config.getNaming());
        builder.setMapKeyNaming(config.getMapKeyNaming());
        builder.setCommenter(config.getCommenter());
        builder.setEntityListenerProvider(config.getEntityListenerProvider());
        builder.setTransactionManager(config.getTransactionManager());
        builder.setBatchSize(config.getBatchSize());
        builder.setFetchSize(config.getFetchSize());
        builder.setMaxRows(config.getMaxRows());
        builder.setQueryTimeout(config.getQueryTimeout());
        return builder;
    }

    public static Builder builder(Core core) {
        Objects.requireNonNull(core);
        Builder builder = new Builder();
        builder.setSqlFileRepository(core.getSqlFileRepository());
        builder.setScriptFileLoader(core.getScriptFileLoader());
        builder.setJdbcLogger(core.getJdbcLogger());
        builder.setRequiresNewController(core.getRequiresNewController());
        builder.setClassHelper(core.getClassHelper());
        builder.setCommandImplementors(core.getCommandImplementors());
        builder.setQueryImplementors(core.getQueryImplementors());
        builder.setExceptionSqlLogType(core.getExceptionSqlLogType());
        builder.setUnknownColumnHandler(core.getUnknownColumnHandler());
        builder.setNaming(core.getNaming());
        builder.setMapKeyNaming(core.getMapKeyNaming());
        builder.setCommenter(core.getCommenter());
        builder.setEntityListenerProvider(core.getEntityListenerProvider());
        builder.setTransactionManager(core.getTransactionManager());
        return builder;
    }

    public static class Core {
        private final SqlFileRepository sqlFileRepository;
        private final ScriptFileLoader scriptFileLoader;
        private final JdbcLogger jdbcLogger;
        private final RequiresNewController requiresNewController;
        private final ClassHelper classHelper;
        private final CommandImplementors commandImplementors;
        private final QueryImplementors queryImplementors;
        private final SqlLogType exceptionSqlLogType;
        private final UnknownColumnHandler unknownColumnHandler;
        private final Naming naming;
        private final MapKeyNaming mapKeyNaming;
        private final Commenter commenter;
        private final EntityListenerProvider entityListenerProvider;
        private final TransactionManager transactionManager;

        public Core(
                SqlFileRepository sqlFileRepository,
                ScriptFileLoader scriptFileLoader,
                JdbcLogger jdbcLogger,
                RequiresNewController requiresNewController,
                ClassHelper classHelper,
                CommandImplementors commandImplementors,
                QueryImplementors queryImplementors,
                SqlLogType exceptionSqlLogType,
                UnknownColumnHandler unknownColumnHandler,
                Naming naming,
                MapKeyNaming mapKeyNaming,
                Commenter commenter,
                EntityListenerProvider entityListenerProvider,
                TransactionManager transactionManager) {
            this.sqlFileRepository = Objects.requireNonNull(sqlFileRepository);
            this.scriptFileLoader = Objects.requireNonNull(scriptFileLoader);
            this.jdbcLogger = Objects.requireNonNull(jdbcLogger);
            this.requiresNewController = Objects.requireNonNull(requiresNewController);
            this.classHelper = Objects.requireNonNull(classHelper);
            this.commandImplementors = Objects.requireNonNull(commandImplementors);
            this.queryImplementors = Objects.requireNonNull(queryImplementors);
            this.exceptionSqlLogType = Objects.requireNonNull(exceptionSqlLogType);
            this.unknownColumnHandler = Objects.requireNonNull(unknownColumnHandler);
            this.naming = Objects.requireNonNull(naming);
            this.mapKeyNaming = Objects.requireNonNull(mapKeyNaming);
            this.commenter = Objects.requireNonNull(commenter);
            this.entityListenerProvider = Objects.requireNonNull(entityListenerProvider);
            this.transactionManager = Objects.requireNonNull(transactionManager);
        }

        public SqlFileRepository getSqlFileRepository() {
            return sqlFileRepository;
        }

        public ScriptFileLoader getScriptFileLoader() {
            return scriptFileLoader;
        }

        public JdbcLogger getJdbcLogger() {
            return jdbcLogger;
        }

        public RequiresNewController getRequiresNewController() {
            return requiresNewController;
        }

        public ClassHelper getClassHelper() {
            return classHelper;
        }

        public CommandImplementors getCommandImplementors() {
            return commandImplementors;
        }

        public QueryImplementors getQueryImplementors() {
            return queryImplementors;
        }

        public SqlLogType getExceptionSqlLogType() {
            return exceptionSqlLogType;
        }

        public UnknownColumnHandler getUnknownColumnHandler() {
            return unknownColumnHandler;
        }

        public Naming getNaming() {
            return naming;
        }

        public MapKeyNaming getMapKeyNaming() {
            return mapKeyNaming;
        }

        public Commenter getCommenter() {
            return commenter;
        }

        public EntityListenerProvider getEntityListenerProvider() {
            return entityListenerProvider;
        }

        public TransactionManager getTransactionManager() {
            return transactionManager;
        }
    }

    public static class Builder {

        private Supplier<String> dataSourceNameSupplier;
        private DataSourceResolver dataSourceResolver;
        private Dialect dialect;
        private SqlFileRepository sqlFileRepository;
        private ScriptFileLoader scriptFileLoader;
        private JdbcLogger jdbcLogger;
        private RequiresNewController requiresNewController;
        private ClassHelper classHelper;
        private CommandImplementors commandImplementors;
        private QueryImplementors queryImplementors;
        private SqlLogType exceptionSqlLogType;
        private UnknownColumnHandler unknownColumnHandler;
        private Naming naming;
        private MapKeyNaming mapKeyNaming;
        private Commenter commenter;
        private EntityListenerProvider entityListenerProvider;
        private TransactionManager transactionManager;
        private int batchSize;
        private int fetchSize;
        private int maxRows;
        private int queryTimeout;

        public Builder setDataSourceNameSupplier(Supplier<String> dataSourceNameSupplier) {
            this.dataSourceNameSupplier = dataSourceNameSupplier;
            return this;
        }

        public Builder setDataSourceResolver(DataSourceResolver dataSourceResolver) {
            this.dataSourceResolver = dataSourceResolver;
            return this;
        }

        public Builder setDialect(Dialect dialect) {
            this.dialect = dialect;
            return this;
        }

        public Builder setSqlFileRepository(SqlFileRepository sqlFileRepository) {
            this.sqlFileRepository = sqlFileRepository;
            return this;
        }

        public Builder setScriptFileLoader(ScriptFileLoader scriptFileLoader) {
            this.scriptFileLoader = scriptFileLoader;
            return this;
        }

        public Builder setJdbcLogger(JdbcLogger jdbcLogger) {
            this.jdbcLogger = jdbcLogger;
            return this;
        }

        public Builder setRequiresNewController(RequiresNewController requiresNewController) {
            this.requiresNewController = requiresNewController;
            return this;
        }

        public Builder setClassHelper(ClassHelper classHelper) {
            this.classHelper = classHelper;
            return this;
        }

        public Builder setCommandImplementors(CommandImplementors commandImplementors) {
            this.commandImplementors = commandImplementors;
            return this;
        }

        public Builder setQueryImplementors(QueryImplementors queryImplementors) {
            this.queryImplementors = queryImplementors;
            return this;
        }

        public Builder setExceptionSqlLogType(SqlLogType exceptionSqlLogType) {
            this.exceptionSqlLogType = exceptionSqlLogType;
            return this;
        }

        public Builder setUnknownColumnHandler(UnknownColumnHandler unknownColumnHandler) {
            this.unknownColumnHandler = unknownColumnHandler;
            return this;
        }

        public Builder setNaming(Naming naming) {
            this.naming = naming;
            return this;
        }

        public Builder setMapKeyNaming(MapKeyNaming mapKeyNaming) {
            this.mapKeyNaming = mapKeyNaming;
            return this;
        }

        public Builder setCommenter(Commenter commenter) {
            this.commenter = commenter;
            return this;
        }

        public Builder setEntityListenerProvider(EntityListenerProvider entityListenerProvider) {
            this.entityListenerProvider = entityListenerProvider;
            return this;
        }

        public Builder setTransactionManager(TransactionManager transactionManager) {
            this.transactionManager = transactionManager;
            return this;
        }

        public Builder setBatchSize(int batchSize) {
            this.batchSize = batchSize;
            return this;
        }

        public Builder setFetchSize(int fetchSize) {
            this.fetchSize = fetchSize;
            return this;
        }

        public Builder setMaxRows(int maxRows) {
            this.maxRows = maxRows;
            return this;
        }

        public Builder setQueryTimeout(int queryTimeout) {
            this.queryTimeout = queryTimeout;
            return this;
        }

        public DomaConfig build() {
            return new DomaConfig(
                    dataSourceNameSupplier,
                    dataSourceResolver,
                    dialect,
                    sqlFileRepository,
                    scriptFileLoader,
                    jdbcLogger,
                    requiresNewController,
                    classHelper,
                    commandImplementors,
                    queryImplementors,
                    exceptionSqlLogType,
                    unknownColumnHandler,
                    naming,
                    mapKeyNaming,
                    commenter,
                    entityListenerProvider,
                    transactionManager,
                    batchSize,
                    fetchSize,
                    maxRows,
                    queryTimeout);
        }
    }
}
