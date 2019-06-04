package toxiproxy.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebConfigIT {

  @Autowired private MockMvc mockMvc;

  @Test
  void notFoundMapping() throws Exception {
    mockMvc.perform(get("/notFound"))
           .andExpect(status().isOk())
           .andExpect(forwardedUrl("/index.html"))
           .andReturn();

    Object handler = mockMvc.perform(get("/something-that-does-not-exist"))
                            .andExpect(status().isNotFound())
                            .andReturn()
                            .getHandler();

    assertNotNull(handler);
    assertTrue(handler instanceof ResourceHttpRequestHandler);
  }
}
