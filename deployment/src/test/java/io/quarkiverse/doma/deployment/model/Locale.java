package io.quarkiverse.doma.deployment.model;

import org.seasar.doma.Embeddable;

@Embeddable
public class Locale {

    public final String language;
    public final String country;

    public Locale(String language, String country) {
        this.language = language;
        this.country = country;
    }
}
