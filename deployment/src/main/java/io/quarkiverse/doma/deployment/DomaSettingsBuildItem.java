package io.quarkiverse.doma.deployment;

import java.util.Objects;

import io.quarkiverse.doma.runtime.DomaSettings;
import io.quarkus.builder.item.SimpleBuildItem;

public final class DomaSettingsBuildItem extends SimpleBuildItem {

    private final DomaSettings settings;

    public DomaSettingsBuildItem(DomaSettings settings) {
        this.settings = Objects.requireNonNull(settings);
    }

    public DomaSettings getSettings() {
        return settings;
    }
}
