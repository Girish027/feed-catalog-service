package feed.catalog.api.response.utils.validator.annotations.implementations;

import feed.catalog.api.response.utils.validator.annotations.interfaces.EnumValidator;

public enum ScheduleConfig implements EnumValidator {
    ADHOC("adhoc"),
    HOURLY("hourly"),
    WEEKLY("weekly"),
    DAILY("daily");

    private String value;

    ScheduleConfig(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
