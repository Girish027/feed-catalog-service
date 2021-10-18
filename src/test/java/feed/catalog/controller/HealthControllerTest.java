package feed.catalog.controller;

import feed.catalog.controllers.HealthCheckController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(value = HealthCheckController.class)
public class HealthControllerTest
{
    MockMvc mockMvc;



    @Before
    public void setup() {
        final HealthCheckController healthController = new HealthCheckController();
        mockMvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    public void getHealth() throws Exception {
        mockMvc.perform(get("/health").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
