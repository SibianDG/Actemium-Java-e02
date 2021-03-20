package domain.enums;

import languages.LanguageResource;

public enum FilterEnum {
    firstname(LanguageResource.getString("firstname").toLowerCase())
    , lastname(LanguageResource.getString("lastname").toLowerCase())
    , Company (LanguageResource.getString("company"))
    ;
    private final String name;

    FilterEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
