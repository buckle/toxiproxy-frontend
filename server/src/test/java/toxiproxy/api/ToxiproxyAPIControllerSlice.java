package toxiproxy.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import toxiproxy.api.service.ToxiproxyAPIService;
import toxiproxy.client.dto.ClientProxy;
import toxiproxy.client.dto.ClientProxyBuilder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static toxiproxy.api.ToxiproxyAPIController.API_ENDPOINT;
import static toxiproxy.api.ToxiproxyAPIController.PROXIES_ENDPOINT;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ToxiproxyAPIControllerSlice {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @SpyBean private ToxiproxyAPIService toxiproxyAPIService;

  @Test
  void getProxies() throws Exception {
    ClientProxy clientProxy = ClientProxyBuilder.builder().build();
    Set<ClientProxy> clientProxies = Sets.newSet(clientProxy);
    when(toxiproxyAPIService.getProxies()).thenReturn(clientProxies);

    String contentAsString = mockMvc.perform(get("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT)
                                                 .secure(true))
                                    .andExpect(status().isOk())
                                    .andReturn()
                                    .getResponse()
                                    .getContentAsString();

    Set<ClientProxy> returnedProxies = Sets.newSet(objectMapper.readValue(contentAsString, ClientProxy[].class));
    assertEquals(1, returnedProxies.size());
    assertTrue(returnedProxies.contains(clientProxy));
  }
}
