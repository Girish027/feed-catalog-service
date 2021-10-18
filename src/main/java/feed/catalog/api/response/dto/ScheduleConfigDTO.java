package feed.catalog.api.response.dto;

import feed.catalog.api.response.utils.validator.annotations.ValidEnumValue;
import feed.catalog.api.response.utils.validator.annotations.implementations.ScheduleConfig;
import feed.catalog.api.response.utils.validator.annotations.interfaces.DTOSymentic;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ScheduleConfigDTO implements DTOSymentic {

    @NotBlank(message = "Schedule config cannot be null!!! Options you can choose from ADHOC , DAILY , WEEKLY , HOURLY , 15MINS .This information is case-sensitive")
    @ValidEnumValue( enumClass = ScheduleConfig.class)
    private String type; // schedule config

    private String cronSchedule;
}
