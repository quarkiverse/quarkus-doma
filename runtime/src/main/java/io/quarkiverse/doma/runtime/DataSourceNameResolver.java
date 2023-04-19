package io.quarkiverse.doma.runtime;

import java.util.Objects;

import jakarta.inject.Singleton;

import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.Unremovable;

public interface DataSourceNameResolver {

    String resolve(String candidateName);

    @Singleton
    @DefaultBean
    @Unremovable
    class DefaultDataSourceNameResolver implements DataSourceNameResolver {

        @Override
        public String resolve(String candidateName) {
            return Objects.requireNonNull(candidateName);
        }
    }
}
