package io.quarkiverse.doma.runtime.devmode;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import io.quarkiverse.doma.runtime.DomaRecorder;
import io.quarkus.dev.spi.HotReplacementContext;
import io.quarkus.dev.spi.HotReplacementSetup;

public class DomaHotReplacementSetup implements HotReplacementSetup {

    @Override
    public void setupHotDeployment(HotReplacementContext hotReplacementContext) {
        Objects.requireNonNull(hotReplacementContext);
        List<Path> resourcesDirs = hotReplacementContext.getResourcesDir();
        DomaRecorder.setHotReplacementResourcesDirs(resourcesDirs);
    }
}
