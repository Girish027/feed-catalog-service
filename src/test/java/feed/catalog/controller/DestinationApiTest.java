package feed.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feed.catalog.api.response.dto.DestinationDTO;
import feed.catalog.api.response.utils.validator.annotations.implementations.DTOModelMapperImpl;
import feed.catalog.controllers.DestinationController;
import feed.catalog.domain.Destination;
import feed.catalog.services.DestinationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(value = DestinationController.class)
public class DestinationApiTest {

    @Mock
    DestinationService destService;
    ObjectMapper objectMapper;
    JavaTimeModule module;

    MockMvc mockMvc;
    static final Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();

    DTOModelMapperImpl dtoModelMapper;
    private final List beanDTOAnnotated = Arrays.asList(new DestinationDTO());

    @Before
    public void setup() {
        final DestinationController destController = new DestinationController();
        destController.setService(destService);
        mockMvc = MockMvcBuilders.standaloneSetup(destController).build();
        objectMapper = new ObjectMapper();
        module = new JavaTimeModule();
        objectMapper.registerModule(module);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        dtoModelMapper = new DTOModelMapperImpl(objectMapper,beanDTOAnnotated);
    }



    @Test
    public void createDestinationTest() throws Exception{

        String payload= "{\"destinationName\":\"destabc1\",\"destinationDisplayName\":\"destabc1\",\"sftpKey\":\"\\/home\\/abc\\/\",\"sinks\":[{\"sinkType\":\"EMAIL\",\"sinkAddress\":\"somepath\",\"config\":{\"key\":\"123\",\"key1\":\"abc\"}}],\"encryption\":{\"type\":\"KEY\",\"keyStore\":\"\\/xyz\\/\"}}";
        DestinationDTO destinationDTO = gson.fromJson(payload,DestinationDTO.class);
        Destination destination = new Destination();
        destination = DTOModelMapperImpl.mapper(destinationDTO,destination);
        destination.setClientId("testClient");
        when(destService.create(destinationDTO,"testClient")).thenReturn(destination);
                 mockMvc.perform(post("/testClient/destination/")
                 .content(objectMapper.writeValueAsString(destinationDTO))
                 .contentType(MediaType.APPLICATION_JSON_VALUE))
                 .andExpect(MockMvcResultMatchers.content().string("{\"content\":\"OK\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/testClient/destination/destabc1\",\"hreflang\":null,\"media\":null,\"title\":null,\"type\":null,\"deprecation\":null}]}"))
                .andExpect(status().isCreated());

    }

    @Test
    public void updateDestinationTest() throws Exception{

        String payload= "{\"destinationName\":\"destabc1\",\"destinationDisplayName\":\"destabc1\",\"sftpKey\":\"\\/home\\/abc\\/\",\"sinks\":[{\"sinkType\":\"EMAIL\",\"sinkAddress\":\"somepath\",\"config\":{\"key\":\"123\",\"key1\":\"abc\"}}],\"encryption\":{\"type\":\"KEY\",\"keyStore\":\"\\/xyz\\/\"}}";
        DestinationDTO destinationDTO = gson.fromJson(payload,DestinationDTO.class);
        Destination destination = new Destination();
        destination = DTOModelMapperImpl.mapper(destinationDTO,destination);
        destination.setClientId("testClient");
        when(destService.update(destinationDTO,"testClient","destabc1")).thenReturn(destination);
        mockMvc.perform(put("/testClient/destination/destabc1")
                .content(objectMapper.writeValueAsString(destinationDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().string("{\"content\":\"destabc1\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/testClient/destination/destabc1\",\"hreflang\":null,\"media\":null,\"title\":null,\"type\":null,\"deprecation\":null}]}"))
                .andExpect(status().isOk());

    }

    @Test
    public void getAllDestinationTest() throws Exception{
         mockMvc.perform(get("/testClient/destinations").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void getDestinationTest() throws Exception{
        String payload= "{\"destinationName\":\"destabc1\",\"destinationDisplayName\":\"destabc1\",\"sinks\":[{\"sinkType\":\"EMAIL\",\"sinkAddress\":\"somepath\",\"config\":{\"key\":\"123\",\"key1\":\"abc\"}}],\"encryption\":{\"type\":\"KEY\",\"keyStore\":\"\\/xyz\\/\"}}";
        DestinationDTO destinationDTO = gson.fromJson(payload,DestinationDTO.class);
        when(destService.getDTO("testClient","destabc1")).thenReturn(destinationDTO);
        mockMvc.perform(get("/testClient/destination/destabc1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":null,\"destinationName\":\"destabc1\",\"destinationDisplayName\":\"destabc1\",\"sinks\":[{\"sinkType\":\"EMAIL\",\"sinkAddress\":\"somepath\",\"config\":{\"key\":\"123\",\"key1\":\"abc\"}}],\"encryption\":{\"type\":\"KEY\",\"keyStore\":\"/xyz/\"}}"))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteDestinationTest() throws Exception{

        mockMvc.perform(put("/testClient/destination/destabc1/delete")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().string("{\"content\":\"Deleted destabc1 for Client testClient\",\"links\":[]}"))
                .andExpect(status().isOk());

    }
}
