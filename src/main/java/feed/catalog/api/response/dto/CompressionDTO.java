package feed.catalog.api.response.dto;

import feed.catalog.api.response.utils.validator.annotations.ValidEnumValue;
import feed.catalog.api.response.utils.validator.annotations.implementations.CompressionConfig;
import feed.catalog.api.response.utils.validator.annotations.interfaces.DTOSymentic;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CompressionDTO implements DTOSymentic {

    @NotBlank(message = "Compression1 cannot be null!!!")
    @ValidEnumValue( enumClass = CompressionConfig.class)
    private String type; // compression config
}
