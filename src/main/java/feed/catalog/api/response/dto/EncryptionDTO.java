package feed.catalog.api.response.dto;

import feed.catalog.api.response.utils.validator.annotations.ValidEnumValue;
import feed.catalog.api.response.utils.validator.annotations.implementations.EncyptionConfig;
import feed.catalog.api.response.utils.validator.annotations.interfaces.DTOSymentic;
import lombok.Data;

@Data
public class EncryptionDTO implements DTOSymentic {

    @ValidEnumValue( enumClass = EncyptionConfig.class)
    private String type;

    private String keyStore;
}
