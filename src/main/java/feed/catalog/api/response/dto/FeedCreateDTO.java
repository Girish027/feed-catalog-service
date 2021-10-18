package feed.catalog.api.response.dto;

import feed.catalog.api.response.utils.validator.annotations.DTO;
import feed.catalog.api.response.utils.validator.annotations.interfaces.DTOSymentic;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@DTO(value = DTOSymentic.class)
public class FeedCreateDTO {

    private String id;

    private String client;

    @NotBlank(message = "Feed name cannot be null!!!")
    @Valid
    private String feedName;

    @NotBlank(message = "Description cannot be null")
    @Valid private String description;

    @NotNull(message = "Active cannot be null; must be either true/false")
    private boolean active;

    @NotNull(message = "Compression details cannot be null")
    @Valid private CompressionDTO compressionConfig; // compression config

    private String platform;

    @NotNull(message ="Schedule Config cannot be null!!! Options you can choose from ADHOC , DAILY , WEEKLY , HOURLY , 15MINS .This information is case-sensitive")
    @Valid private ScheduleConfigDTO scheduleConfig; // schedule config

    @NotBlank(message ="Destination is mandatory for feed")
    private String destinationName;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime editedAt = LocalDateTime.now();

    @Valid private ExporterFormatDTO exportFormat; // export format config

    @NotNull(message ="At least one report name must be tagged with the Feed")
    private List<ReportsDTO> reportList;

    @Override
    public String toString() {
        return  "\n\t Feed name = " + feedName + '\'' +
                "\n\t Description = " + description + '\'' +
                "\n\t isCompressed = " + compressionConfig +
                "\n\t platform = " + platform + '\'' +
                "\n\t scheduleConfig = " + scheduleConfig +
                "\n\t format = " + exportFormat + '\'' +
                "\n\t reportList = " + reportList + "";
    }
}
