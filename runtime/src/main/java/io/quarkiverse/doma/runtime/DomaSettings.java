package io.quarkiverse.doma.runtime;

import static java.util.stream.Collectors.toMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.NoCacheSqlFileRepository;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.dialect.Db2Dialect;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.dialect.MssqlDialect;
import org.seasar.doma.jdbc.dialect.MysqlDialect;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.SqliteDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

public class DomaSettings {

    public SqlFileRepositoryType sqlFileRepository;
    public NamingType naming;
    public SqlLogType exceptionSqlLogType;
    public List<DataSourceSettings> dataSources;

    public Map<String, String> asNamedSqlLoadScripts() {
        return dataSources.stream()
                .filter(it -> it.sqlLoadScript != null)
                .collect(toMap(it -> it.name, it -> it.sqlLoadScript, (a, b) -> a, LinkedHashMap::new));
    }

    public static class DataSourceSettings {
        public String name;
        public boolean isDefault;
        public DialectType dialect;
        public int batchSize;
        public int fetchSize;
        public int maxRows;
        public int queryTimeout;
        public String sqlLoadScript;

        @Override
        public String toString() {
            return "DataSourceSettings{"
                    + "name='"
                    + name
                    + '\''
                    + ", isDefault="
                    + isDefault
                    + ", dialect="
                    + dialect
                    + ", batchSize="
                    + batchSize
                    + ", fetchSize="
                    + fetchSize
                    + ", maxRows="
                    + maxRows
                    + ", queryTimeout="
                    + queryTimeout
                    + ", sqlLoadScript='"
                    + sqlLoadScript
                    + '\''
                    + '}';
        }
    }

    public enum DialectType {
        DB2(Db2Dialect::new),
        H2(H2Dialect::new),
        MSSQL(MssqlDialect::new),
        MYSQL(MysqlDialect::new),
        ORACLE(OracleDialect::new),
        POSTGRES(PostgresDialect::new),
        STANDARD(StandardDialect::new),
        SQLITE(SqliteDialect::new);

        private final Supplier<Dialect> constructor;

        DialectType(Supplier<Dialect> constructor) {
            this.constructor = constructor;
        }

        public Dialect create() {
            return this.constructor.get();
        }
    }

    public enum SqlFileRepositoryType {
        NO_CACHE(NoCacheSqlFileRepository::new),
        GREEDY_CACHE(GreedyCacheSqlFileRepository::new);

        private final Supplier<SqlFileRepository> constructor;

        SqlFileRepositoryType(Supplier<SqlFileRepository> constructor) {
            this.constructor = constructor;
        }

        public SqlFileRepository create() {
            return this.constructor.get();
        }
    }

    public enum NamingType {
        NONE(Naming.NONE),
        LOWER_CASE(Naming.LOWER_CASE),
        UPPER_CASE(Naming.UPPER_CASE),
        SNAKE_LOWER_CASE(Naming.SNAKE_LOWER_CASE),
        SNAKE_UPPER_CASE(Naming.SNAKE_UPPER_CASE);

        private final Naming naming;

        NamingType(Naming naming) {
            this.naming = naming;
        }

        public Naming naming() {
            return this.naming;
        }
    }

    @Override
    public String toString() {
        return "DomaSettings{"
                + "sqlFileRepository="
                + sqlFileRepository
                + ", naming="
                + naming
                + ", exceptionSqlLogType="
                + exceptionSqlLogType
                + ", dataSources="
                + dataSources
                + '}';
    }
}
