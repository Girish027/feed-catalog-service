package feed.catalog.api.response.dto;

import feed.catalog.api.response.utils.validator.annotations.ValidEnumValue;
import feed.catalog.api.response.utils.validator.annotations.implementations.SinkTypeConfig;
import feed.catalog.api.response.utils.validator.annotations.interfaces.DTOSymentic;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class SinkDTO implements DTOSymentic{

    @Valid
    @ValidEnumValue( enumClass = SinkTypeConfig.class)
    private String sinkType;

    @NotBlank
    @Valid
    private String sinkAddress;

    @Nullable
    private Map<String,String> config;
}
