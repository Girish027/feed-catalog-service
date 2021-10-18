package feed.catalog.api.response.dto;

import feed.catalog.api.response.utils.validator.annotations.interfaces.DTOSymentic;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReportsDTO implements DTOSymentic {

    @NotBlank(message = "Report should be a valid name")
    private String reportName;

    @NotBlank(message = "Scheme is mandatory for Feed. For example if report is generated in HDFS , scheme will be hdfs://")
    private String sourceScheme;

    @NotBlank(message = "Path of the report is manditory")
    private String sourcePath; // Root folder for report

    @NotBlank(message = "If it is dynamic or statis")
    private String loadStrategy; // Dynamic or statis path

}
