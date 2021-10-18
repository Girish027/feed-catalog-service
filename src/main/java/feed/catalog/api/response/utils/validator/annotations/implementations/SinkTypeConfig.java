package feed.catalog.api.response.utils.validator.annotations.implementations;

import feed.catalog.api.response.utils.validator.annotations.interfaces.EnumValidator;

public enum SinkTypeConfig implements EnumValidator {
    EMAIL("email"),
    SFTP("sftp");

    private String value;

    SinkTypeConfig(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}