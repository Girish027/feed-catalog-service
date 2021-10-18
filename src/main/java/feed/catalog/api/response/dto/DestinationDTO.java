package feed.catalog.api.response.dto;

import feed.catalog.api.response.utils.validator.annotations.DTO;
import feed.catalog.api.response.utils.validator.annotations.interfaces.DTOSymentic;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@DTO(value = DTOSymentic.class)
public class DestinationDTO {

    private String id;

    @NotBlank(message = "Destination Name cannot be empty!!")
    @Valid
    private String destinationName;

    private String destinationDisplayName;

    @NotNull(message = "Sink cannot be Null")
    private List<SinkDTO> sinks;

    @Nullable
    private EncryptionDTO encryption;

}
