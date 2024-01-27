package io.quarkiverse.doma.runtime.devmode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.seasar.doma.jdbc.AbstractSqlFileRepository;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.dialect.Dialect;

public class HotReplacementSqlFileRepository extends AbstractSqlFileRepository {

    private final List<Path> resourcesDirs;

    public HotReplacementSqlFileRepository(List<Path> resourcesDirs) {
        Objects.requireNonNull(resourcesDirs);
        this.resourcesDirs = Collections.unmodifiableList(resourcesDirs);
    }

    @Override
    protected SqlFile getSqlFileWithCacheControl(Method method, String path, Dialect dialect) {
        return createSqlFile(method, path, dialect);
    }

    @Override
    protected String getSql(String path) {
        for (Path dir : resourcesDirs) {
            Path file = dir.resolve(path);
            if (Files.exists(file)) {
                try {
                    return Files.readString(file);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
        return null;
    }
}
