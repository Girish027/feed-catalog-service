package feed.catalog.api.response.utils.validator.annotations.implementations;

import feed.catalog.api.response.utils.validator.annotations.interfaces.EnumValidator;

public enum ExporterFormat implements EnumValidator {
    CSV("csv"),
    EXCEL("excel"),
    TSV("tsv");
    private String value;

    ExporterFormat(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
