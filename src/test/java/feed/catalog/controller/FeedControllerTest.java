package feed.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feed.catalog.api.response.dto.DestinationDTO;
import feed.catalog.api.response.dto.FeedCreateDTO;
import feed.catalog.api.response.dto.FeedSnapShot;
import feed.catalog.api.response.dto.FeedUpdateDTO;
import feed.catalog.api.response.utils.validator.annotations.implementations.DTOModelMapperImpl;
import feed.catalog.controllers.FeedController;
import feed.catalog.domain.Destination;
import feed.catalog.domain.Feed;
import feed.catalog.services.FeedServiceImp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(controllers = FeedController.class)
@SuppressWarnings(value="unchecked")
public class FeedControllerTest {

    @Autowired
    private ApplicationContext applicationContext;

    File file;

    Reader reader;

    @Mock
    FeedServiceImp serviceImp;
    ObjectMapper objectMapper;
    JavaTimeModule module;
    MockMvc mockMvc;
    static final Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    DTOModelMapperImpl dtoModelMapper;
    private final List beanDTOAnnotated = Arrays.asList(new FeedCreateDTO(),new FeedUpdateDTO(),new DestinationDTO(),new FeedSnapShot());

    @Before
    public void setup() {
        final FeedController feedController = new FeedController();
        feedController.setFeedService(serviceImp);
        mockMvc = MockMvcBuilders.standaloneSetup(feedController).build();
        objectMapper = new ObjectMapper();
        module = new JavaTimeModule();
        objectMapper.registerModule(module);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        dtoModelMapper = new DTOModelMapperImpl(objectMapper,beanDTOAnnotated);
    }


    @Test
    public void createFeed() throws Exception {
        String jsonValue = "{ \n" +
                "    \"description\": \"Preceptor repository is running TEST for create\",\n" +
                "    \"feedName\": \"OptusSalesTeam_B\",\n" +
                "    \"scheduleConfig\":{\"type\": \"DAILY\"},\n" +
                "    \"compressionConfig\":{\"type\": \"ZIP\"},\n" +
                "    \"platform\": \"DP2\",\n" +
                "    \"exportFormat\":{\"type\":\"CSV\"},\n" +
                "    \"destinationName\":\"12345\",\n" +
                "    \"reportList\": [{\n" +
                "      \"reportName\":\"Collab-Tagging-Detailed\",\n" +
                "      \"sourceScheme\":\"hdfs\",\n" +
                "      \"sourcePath\":\"/OutPutPath/x/y/z\",\n" +
                "     \"loadStrategy\":\"Dynamic\"},{\n" +
                "      \"reportName\":\"Collab-Tagging-Summary\",\n" +
                "      \"sourceScheme\":\"hdfs\",\n" +
                "      \"sourcePath\":\"/OutPutPath/x/y/z\",\n" +
                "     \"loadStrategy\":\"Dynamic\"}]\n" +
                "\n" +
                "}";

        FeedCreateDTO feedCreateDTO = gson.fromJson(jsonValue,FeedCreateDTO.class);
        Feed feed = new Feed();
        feed.setFeedName("OptusSalesTeam_B");
        feed.setCreatedAt(feedCreateDTO.getCreatedAt());
        feed.setEditedAt(feedCreateDTO.getEditedAt());
        feed.setDescription("Preceptor repository is running TEST for create");
        feed.setCompressionConfig(gson.toJson(feedCreateDTO.getCompressionConfig()));
        feed.setReportList(Arrays.asList(gson.toJson(feedCreateDTO.getReportList())));

        when(serviceImp.create(feedCreateDTO,String.valueOf("client_info_1234"))).thenReturn(feed);

        mockMvc.perform(post("/client_info_1234/feeds")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(feedCreateDTO)))
                .andExpect(MockMvcResultMatchers.content().string("{\"content\":\"OptusSalesTeam_B\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/client_info_1234/feeds/OptusSalesTeam_B\",\"hreflang\":null,\"media\":null,\"title\":null,\"type\":null,\"deprecation\":null}]}"));

        ArgumentCaptor<FeedCreateDTO> feedDTO = ArgumentCaptor.forClass(FeedCreateDTO.class);
        verify(serviceImp).create(feedDTO.capture(),eq("client_info_1234"));
        FeedCreateDTO feedCreateDTOActual = feedDTO.getValue();
        assertThat(feedCreateDTOActual.getDescription(), equalTo(feedCreateDTO.getDescription()));
        assertThat(feedCreateDTOActual.getReportList(), equalTo(feedCreateDTO.getReportList()));

        verifyNoMoreInteractions(serviceImp);
    }

    @Test
    public void updateFeed() throws Exception {
        String jsonValue = "{ \n" +
                "    \"description\": \"Preceptor repository is running TEST for update\",\n" +
                "    \"feedName\": \"OptusSalesTeam_B\",\n" +
                "    \"scheduleConfig\":{\"type\": \"DAILY\"},\n" +
                "    \"compressionConfig\":{\"type\": \"ZIP\"},\n" +
                "    \"platform\": \"DP2\",\n" +
                "    \"exportFormat\":{\"type\":\"CSV\"},\n" +
                "    \"destinationName\":\"12345\",\n" +
                "    \"reportList\": [{\n" +
                "      \"reportName\":\"Collab-Tagging-Detailed\",\n" +
                "      \"sourceScheme\":\"hdfs\",\n" +
                "      \"sourcePath\":\"/OutPutPath/x/y/z\",\n" +
                "     \"loadStrategy\":\"Dynamic\"},{\n" +
                "      \"reportName\":\"Collab-Tagging-Summary\",\n" +
                "      \"sourceScheme\":\"hdfs\",\n" +
                "      \"sourcePath\":\"/OutPutPath/x/y/z\",\n" +
                "     \"loadStrategy\":\"Dynamic\"}]\n" +
                "\n" +
                "}";

        FeedUpdateDTO feedUpdateDTO = gson.fromJson(jsonValue,FeedUpdateDTO.class);
        Feed feed = new Feed();
        feed.setFeedName("OptusSalesTeam_B");
        feed.setEditedAt(feedUpdateDTO.getEditedAt());
        feed.setDescription("Preceptor repository is running TEST for update");
        feed.setCompressionConfig(gson.toJson(feedUpdateDTO.getCompressionConfig()));
        feed.setReportList(Arrays.asList(gson.toJson(feedUpdateDTO.getReportList())));

        /*when(feedDAO.updateFeed(feed)).thenReturn(feed);
        when(serviceImp.update(feed)).thenReturn(feed);*/
        when(serviceImp.update(feedUpdateDTO,String.valueOf("OptusSalesTeam_B"),String.valueOf("client_info_1234"))).thenReturn(feed);


        mockMvc.perform(put("/client_info_1234/feeds/OptusSalesTeam_B")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(feedUpdateDTO)))
                .andExpect(MockMvcResultMatchers.content().string("{\"content\":\"OptusSalesTeam_B\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/client_info_1234/feeds/OptusSalesTeam_B\",\"hreflang\":null,\"media\":null,\"title\":null,\"type\":null,\"deprecation\":null}]}"));

        ArgumentCaptor<FeedUpdateDTO> feedDTO = ArgumentCaptor.forClass(FeedUpdateDTO.class);
        verify(serviceImp).update(feedDTO.capture(),eq("OptusSalesTeam_B"),eq("client_info_1234"));
        FeedUpdateDTO feedUpdateDTOActual = feedDTO.getValue();
        assertThat(feedUpdateDTOActual.getDescription(), equalTo(feedUpdateDTO.getDescription()));
        assertThat(feedUpdateDTOActual.getReportList(), equalTo(feedUpdateDTO.getReportList()));

        verifyNoMoreInteractions(serviceImp);
    }

    @Test
    public void getFeed() throws Exception {

        String jsonValue = "{\"id\":\"8bc6088c888b1d9a5444362d862c695e\",\"client\":null,\"feedName\":\"OptusSalesTeam_B\",\"description\":\"OptusClientforBTeam\",\"active\":false,\"compressionConfig\":{\"type\":\"ZIP\"},\"platform\":\"DP2\",\"scheduleConfig\":{\"type\":\"DAILY\",\"cronSchedule\":null},\"destinationName\":\"12345\",\"createdAt\":[2019,8,16,20,29,2,160000000],\"editedAt\":[2019,8,16,20,29,2,160000000],\"exportFormat\":{\"type\":\"CSV\"},\"reportList\":[{\"reportName\":\"Collab-Tagging-Detailed\",\"sourceScheme\":\"hdfs\",\"sourcePath\":\"/OutPutPath/x/y/z\",\"loadStrategy\":\"Dynamic\"}],\"wfId\":null,\"nominalTime\":null,\"destination\":null}";
        Feed feed = new Feed();
        FeedSnapShot feedCreateDTO = objectMapper.readValue(jsonValue,FeedSnapShot.class);
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
        feed.setCompressionConfig(gson.toJson(feedCreateDTO.getCompressionConfig()));
        feed.setReportList(Arrays.asList("[\n  {\n    \"reportName\": \"Collab-Tagging-Detailed\",\n    \"sourceScheme\": \"hdfs\",\n    \"sourcePath\": \"/OutPutPath/x/y/z\",\n    \"loadStrategy\": \"Dynamic\"\n  }\n]"));

        Optional<Feed> feedOptional =  Optional.ofNullable(feed);

        when(serviceImp.getDTO(String.valueOf("OptusSalesTeam_B"),String.valueOf("client_info_1234"))).thenReturn(feedCreateDTO);

        mockMvc.perform(get("/client_info_1234/feeds/OptusSalesTeam_B")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(jsonValue));

        ArgumentCaptor<FeedCreateDTO> feedDTO = ArgumentCaptor.forClass(FeedCreateDTO.class);
        verify(serviceImp).getDTO(eq("OptusSalesTeam_B"),eq("client_info_1234"));
        verifyNoMoreInteractions(serviceImp);
    }

    @Test
    public void getFeedSnapShot() throws Exception {

        String jsonValue = "{\"id\":\"55e4f430d7882f93a570edfef3c42b96\",\"client\":null,\"feedName\":\"Collab_Tagging_Team_D\",\"description\":\"Optus Client\",\"active\":false,\"compressionConfig\":{\"type\":\"ZIP\"},\"platform\":\"DP2\",\"scheduleConfig\":{\"type\":\"ADHOC\",\"cronSchedule\":null},\"destinationName\":\"destabc2\",\"createdAt\":[2019,8,23,1,31,56,428000000],\"editedAt\":[2019,8,23,1,31,56,428000000],\"exportFormat\":{\"type\":\"EXCEL\"},\"reportList\":[{\"reportName\":\"report1\",\"sourceScheme\":\"hdfs\",\"sourcePath\":\"\\\\OutPutPath\\\\x\\\\y\\\\z\",\"loadStrategy\":\"Dynamic\"}],\"wfId\":null,\"nominalTime\":null,\"destination\":{\"id\":\"client_info_6c85230a-a14a-4bb1-8d1e-8f93ab70af345678destabc2\",\"destinationName\":\"destabc2\",\"destinationDisplayName\":\"destabc1\",\"sinks\":[{\"sinkType\":\"EMAIL\",\"sinkAddress\":\"somepath\",\"config\":{\"key\":\"123\",\"key1\":\"abc\",\"sftpKey\":\"/home/abc/\"}}],\"encryption\":{\"type\":\"KEY\",\"keyStore\":\"/xyz/\"}}}";
        Feed feed = new Feed();
        FeedSnapShot feedCreateDTO = objectMapper.readValue(jsonValue,FeedSnapShot.class);
        feed.setId("55e4f430d7882f93a570edfef3c42b96");
        feed.setFeedName("Collab_Tagging_Team_D");
        feed.setClientId("client_info_6c85230a-a14a-4bb1-8d1e-8f93ab70af345678");
        feed.setCompressionConfig("{\n  \"type\": \"ZIP\"\n}");
        feed.setPlatform("DP2");
        feed.setScheduleConfig("{\n  \"type\": \"ADHOC\"\n}");
        feed.setExportFormat("{\n  \"type\": \"EXCEL\"\n}");
        feed.setCreatedAt(feedCreateDTO.getCreatedAt());
        feed.setEditedAt(feedCreateDTO.getEditedAt());
        feed.setDestinationName("destabc2");
        feed.setDescription("Optus Client");
        feed.setCompressionConfig(gson.toJson(feedCreateDTO.getCompressionConfig()));
        feed.setReportList(Arrays.asList(gson.toJson(feedCreateDTO.getReportList())));

        Destination dest = DTOModelMapperImpl.mapper(feedCreateDTO.getDestination(),new Destination());

        dest.setClientId("client_info_6c85230a-a14a-4bb1-8d1e-8f93ab70af345678");

        when(serviceImp.getFeedSnapShot(String.valueOf("OptusSalesTeam_B"),String.valueOf("client_info_1234"))).thenReturn(feedCreateDTO);

        mockMvc.perform(get("/client_info_1234/feeds/OptusSalesTeam_B/cadence-feed-snapshot")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(jsonValue));

        ArgumentCaptor<String> clientid = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> name = ArgumentCaptor.forClass(String.class);
        verify(serviceImp).getFeedSnapShot(name.capture(), clientid.capture());
        String feedCreateNameActual = name.getValue();
        String feedCreateClientActual = clientid.getValue();
        assertThat(feedCreateNameActual, equalTo("OptusSalesTeam_B"));
        assertThat(feedCreateClientActual, equalTo("client_info_1234"));

        verifyNoMoreInteractions(serviceImp);
    }

    @Test
    public void getAllDestinationTest() throws Exception{
        mockMvc.perform(get("/client_info_1234/feeds").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
