package feed.catalog.api.response.utils.validator.annotations.implementations;

import feed.catalog.api.response.utils.validator.annotations.interfaces.EnumValidator;

public enum EncyptionConfig implements EnumValidator {
    KEY("key"),
    PASSWORD("password");

    private String value;

    EncyptionConfig(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
