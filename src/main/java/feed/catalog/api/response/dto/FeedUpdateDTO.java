package feed.catalog.api.response.dto;

import feed.catalog.api.response.utils.validator.annotations.DTO;
import feed.catalog.api.response.utils.validator.annotations.interfaces.DTOSymentic;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Data
@DTO(value = DTOSymentic.class)
public class FeedUpdateDTO implements DTOSymentic {

    private String description;

    @Nullable
    @Valid private CompressionDTO compressionConfig; // compression config

    @Nullable
    @Valid private ScheduleConfigDTO scheduleConfig; // schedule config

    private String destinationName;

    private int isDeleted;

    @UpdateTimestamp
    private LocalDateTime editedAt ;

    private boolean active;

    @Nullable
    @Valid private ExporterFormatDTO exportFormat; // export format config

    @Nullable
    @Valid private List<ReportsDTO> reportList;

    @Override
    public String toString() {
        return  "\n\t Description = " + description + '\'' +
                "\n\t isCompressed = " + compressionConfig +
                "\n\t scheduleConfig = " + scheduleConfig +
                "\n\t format = " + exportFormat + '\'' +
                "\n\t reportList = " + reportList + "";
    }
}
