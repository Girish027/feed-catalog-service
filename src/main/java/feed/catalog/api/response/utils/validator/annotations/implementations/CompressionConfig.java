package feed.catalog.api.response.utils.validator.annotations.implementations;

import feed.catalog.api.response.utils.validator.annotations.interfaces.EnumValidator;

public enum CompressionConfig implements EnumValidator {
    ZIP("zip");

    private String value;

    CompressionConfig(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
