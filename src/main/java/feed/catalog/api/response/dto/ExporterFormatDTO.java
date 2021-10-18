package feed.catalog.api.response.dto;

import feed.catalog.api.response.utils.validator.annotations.ValidEnumValue;
import feed.catalog.api.response.utils.validator.annotations.implementations.ExporterFormat;
import feed.catalog.api.response.utils.validator.annotations.interfaces.DTOSymentic;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ExporterFormatDTO implements DTOSymentic {
    /*@Pattern(regexp = "(?:^|(?<= ))(CSV|JSON)(?:(?= )|$)",
            message = "format should be one of these value: CSV , JSON. This information is case-sensitive")*/
    @NotBlank(message ="Exporter format cannot be null. Format should be one of these value: CSV , JSON. This information is case-sensitive.")
    @ValidEnumValue( enumClass = ExporterFormat.class)
    private String type ;
}
