package feed.catalog.api.response.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import feed.catalog.api.response.dto.DestinationDTO;
import feed.catalog.api.response.dto.FeedCreateDTO;
import feed.catalog.api.response.utils.validator.annotations.DTO;
import feed.catalog.api.response.utils.validator.annotations.interfaces.DTOSymentic;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@DTO(value = DTOSymentic.class)
public class FeedSnapShot extends FeedCreateDTO {

    private String wfId;
    private String nominalTime;
    private DestinationDTO destination;

    }
