package io.quarkiverse.doma.runtime;

import org.seasar.doma.jdbc.SqlBuilderSettings;

final class DefaultSqlBuilderSettings implements SqlBuilderSettings {
    private final boolean shouldRemoveBlankLines;
    private final boolean shouldRequireInListPadding;

    public DefaultSqlBuilderSettings(boolean shouldRemoveBlankLines, boolean shouldRequireInListPadding) {
        this.shouldRemoveBlankLines = shouldRemoveBlankLines;
        this.shouldRequireInListPadding = shouldRequireInListPadding;
    }

    @Override
    public boolean shouldRemoveBlankLines() {
        return shouldRemoveBlankLines;
    }

    @Override
    public boolean shouldRequireInListPadding() {
        return shouldRequireInListPadding;
    }
}
