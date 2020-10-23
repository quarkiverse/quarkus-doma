package io.quarkiverse.doma.runtime.devmode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.seasar.doma.jdbc.ScriptFileLoader;

public class HotReplacementScriptFileLoader implements ScriptFileLoader {

    private final List<Path> resourcesDirs;

    public HotReplacementScriptFileLoader(List<Path> resourcesDirs) {
        Objects.requireNonNull(resourcesDirs);
        this.resourcesDirs = Collections.unmodifiableList(resourcesDirs);
    }

    @Override
    public URL loadAsURL(String path) {
        for (Path dir : resourcesDirs) {
            Path file = dir.resolve(path);
            if (Files.exists(file)) {
                try {
                    return file.toUri().toURL();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
        return null;
    }
}
