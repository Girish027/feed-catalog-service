package feed.catalog.utils.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import feed.catalog.api.response.dto.*;
import feed.catalog.api.response.utils.validator.annotations.implementations.DTOModelMapperImpl;
import feed.catalog.domain.Destination;
import feed.catalog.domain.Feed;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DTOModelMapperImpTest {

    @Autowired
    private ApplicationContext applicationContext;
    ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().applicationContext(this.applicationContext).build();
    DTOModelMapperImpl dtoModelMapper;
    private final List beanDTOAnnotated = Arrays.asList(new FeedCreateDTO(),new FeedUpdateDTO());

    @Before
    public void before(){
        dtoModelMapper = new DTOModelMapperImpl(objectMapper,beanDTOAnnotated);
    }

    @Test
    public void whenConvertDTOtoEntity_thenCorrect(){

        CompressionDTO compressionDTO = new CompressionDTO();
        compressionDTO.setType("ZIP");

        ReportsDTO reportsDTO = new ReportsDTO();
        reportsDTO.setReportName("View_DCF");
        reportsDTO.setLoadStrategy("Dynamic");
        reportsDTO.setSourcePath("/d/d/fd/asd/qwe");

        ReportsDTO reportsDTO1 = new ReportsDTO();
        reportsDTO.setReportName("View_DCF_1");
        reportsDTO.setLoadStrategy("Dynamic_1");
        reportsDTO.setSourcePath("/d/d/fd/asd/qwe/1");

        ArrayList<ReportsDTO> reportsDTOList = new ArrayList<>();
        reportsDTOList.add(reportsDTO);
        reportsDTOList.add(reportsDTO1);

        FeedCreateDTO feedCreateDTO = new FeedCreateDTO();
        feedCreateDTO.setCreatedAt(LocalDateTime.now());
        feedCreateDTO.setEditedAt(LocalDateTime.now(ZoneId.systemDefault()));
        feedCreateDTO.setDescription("Preceptor repository is running");
        feedCreateDTO.setCompressionConfig(compressionDTO);
        feedCreateDTO.setReportList(reportsDTOList);

        Feed feed = new Feed();
        DTOModelMapperImpl.mapper(feedCreateDTO,feed);
        assertEquals(feedCreateDTO.getCreatedAt(), feed.getCreatedAt());
        assertEquals(feedCreateDTO.getEditedAt(), feed.getEditedAt());
        assertEquals(feedCreateDTO.getDescription(), feed.getDescription());
        assertNotEquals(feedCreateDTO.getCompressionConfig().toString(), feed.getCompressionConfig());
        assertNotEquals(feedCreateDTO.getReportList().toArray(), feed.getReportList());
    }

    @Test
    public void whenConvertEntityToDTO_thencorrect() throws Exception {
        String jsonValue = "{\"id\":\"8bc6088c888b1d9a5444362d862c695e\",\"client\":null,\"feedName\":\"OptusSalesTeam_B\",\"description\":\"OptusClientforBTeam\",\"active\":false,\"compressionConfig\":{\"type\":\"ZIP\"},\"platform\":\"DP2\",\"scheduleConfig\":{\"type\":\"DAILY\"},\"destinationName\":\"12345\",\"createdAt\":[2019,8,16,20,29,2,160000000],\"editedAt\":[2019,8,16,20,29,2,160000000],\"exportFormat\":{\"type\":\"CSV\"},\"reportList\":[{\"reportName\":\"Collab-Tagging-Detailed\",\"sourceScheme\":\"hdfs\",\"sourcePath\":\"/OutPutPath/x/y/z\",\"loadStrategy\":\"Dynamic\"}],\"wfId\":null,\"nominalTime\":null,\"destination\":null}";
        Feed feed = new Feed();
        FeedCreateDTO feedCreateDTO = objectMapper.readValue(jsonValue,FeedCreateDTO.class);
        feed.setId("8bc6088c888b1d9a5444362d862c695e");
        feed.setFeedName("OptusSalesTeam_B");
        feed.setClientId("client_info_6c85230a-a14a-4bb1-8d1e-8f93ab70af34567");
        feed.setCompressionConfig("{\n  \"type\": \"ZIP\"\n}");
        feed.setPlatform("DP2");
        feed.setScheduleConfig("{\n  \"type\": \"DAILY\"\n}");
        feed.setExportFormat("{\n  \"type\": \"DAILY\"\n}");
        feed.setCreatedAt(feedCreateDTO.getCreatedAt());
        feed.setEditedAt(feedCreateDTO.getEditedAt());
        feed.setDestinationName("destinationName");
        feed.setDescription("Optus Client for B Team");
        feed.setCompressionConfig(objectMapper.writeValueAsString(feedCreateDTO.getCompressionConfig()));
        feed.setReportList(Arrays.asList("[\n  {\n    \"reportName\": \"Collab-Tagging-Detailed\",\n    \"sourceScheme\": \"hdfs\",\n    \"sourcePath\": \"/OutPutPath/x/y/z\",\n    \"loadStrategy\": \"Dynamic\"\n  }\n]"));

        feed = DTOModelMapperImpl.mapper(feedCreateDTO,feed);
        FeedCreateDTO feedCreateDTO1 = DTOModelMapperImpl.mapper(feed,new FeedCreateDTO());
        assertEquals(feedCreateDTO1.getReportList(),feedCreateDTO.getReportList());
        assertEquals(feedCreateDTO1,feedCreateDTO);

    }

    @Test
    public void whenConvertEntityToDTO_thenincorrect() throws Exception {
        String jsonValue = "{\"id\":\"8bc6088c888b1d9a5444362d862c695e\",\"client\":null,\"feedName\":\"OptusSalesTeam_B\",\"description\":\"OptusClientforBTeam\",\"active\":false,\"compressionConfig\":{\"type\":\"ZIP\"},\"platform\":\"DP2\",\"scheduleConfig\":{\"type\":\"DAILY\"},\"destinationName\":\"12345\",\"createdAt\":[2019,8,16,20,29,2,160000000],\"editedAt\":[2019,8,16,20,29,2,160000000],\"exportFormat\":{\"type\":\"CSV\"},\"reportList\":[{\"reportName\":\"Collab-Tagging-Detailed\",\"sourceScheme\":\"hdfs\",\"sourcePath\":\"/OutPutPath/x/y/z\",\"loadStrategy\":\"Dynamic\"}],\"wfId\":null,\"nominalTime\":null,\"destination\":null}";
        Feed feed = new Feed();
        FeedSnapShot feedCreateDTO = objectMapper.readValue(jsonValue,FeedSnapShot.class);
        feed.setPlatform("DP2");
        feed.setScheduleConfig("{\n  \"type\": \"DAILY\"\n}");
        feed.setExportFormat("{\n  \"type\": \"DAILY\"\n}");
        feed.setCreatedAt(feedCreateDTO.getCreatedAt());
        feed.setEditedAt(feedCreateDTO.getEditedAt());
        feed.setDestinationName("destinationName");
        feed.setDescription("Optus Client for B Team");
        assertThrows(Exception.class, ()->
                DTOModelMapperImpl.mapper(feed,new SinkDTO()));

    }

    @Test
    public void whenConvertDTOToDTO_thenincorrect() throws Exception {
        String jsonValue = "{\"id\":\"8bc6088c888b1d9a5444362d862c695e\",\"client\":null,\"feedName\":\"OptusSalesTeam_B\",\"description\":\"OptusClientforBTeam\",\"active\":false,\"compressionConfig\":{\"type\":\"ZIP\"},\"platform\":\"DP2\",\"scheduleConfig\":{\"type\":\"DAILY\"},\"destinationName\":\"12345\",\"createdAt\":[2019,8,16,20,29,2,160000000],\"editedAt\":[2019,8,16,20,29,2,160000000],\"exportFormat\":{\"type\":\"CSV\"},\"reportList\":[{\"reportName\":\"Collab-Tagging-Detailed\",\"sourceScheme\":\"hdfs\",\"sourcePath\":\"/OutPutPath/x/y/z\",\"loadStrategy\":\"Dynamic\"}],\"wfId\":null,\"nominalTime\":null,\"destination\":null}";
        FeedSnapShot feedCreateDTO = objectMapper.readValue(jsonValue,FeedSnapShot.class);
        assertNotEquals(DTOModelMapperImpl.mapperDTO(feedCreateDTO,new FeedCreateDTO()), feedCreateDTO);
        assertEquals(DTOModelMapperImpl.mapperDTO(feedCreateDTO,new FeedCreateDTO()).getFeedName(), feedCreateDTO.getFeedName());

    }

    public void whenDTOToEntityDestination() throws Exception{
        String jsonValue = "{\n" +
                "     \"destinationName\":\"destabc1\",\n" +
                "     \"destinationDisplayName\":\"destabc1\",\n" +
                "     \"sinks\":[\n" +
                "        {\n" +
                "           \"sinkType\":\"SFTP\",\n" +
                "           \"sinkAddress\":\"dev-insights-customreporting01.app.shared.int.sv2.247-inc.net\",\n" +
                "           \"config\":{\n" +
                "              \"sftpPort\":\"22\",\n" +
                "              \"sftpKey\":\"/home/sbasak/.ssh/id_rsa\",\n" +
                "              \"sftpDirectory\":\"/home/sbasak/\",\n" +
                "              \"sftpUser\":\"sbasak\"\n" +
                "           }\n" +
                "        }\n" +
                "     ],\n" +
                "     \"encryption\":{\n" +
                "        \"type\":\"KEY\",\n" +
                "        \"keyStore\":\"/home/sbasak/publicKey.asc\"\n" +
                "     }\n" +
                "  }";
        DestinationDTO destinationDTO = objectMapper.readValue(jsonValue,DestinationDTO.class);
        DTOModelMapperImpl.mapper(destinationDTO, new Destination());

    }
}

