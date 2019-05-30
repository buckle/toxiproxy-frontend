package toxiproxy.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import toxiproxy.api.service.ToxiproxyAPIService;
import toxiproxy.client.dto.ClientProxy;
import toxiproxy.client.dto.ClientProxyBuilder;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static toxiproxy.api.controller.ToxiproxyAPIController.API_ENDPOINT;
import static toxiproxy.api.controller.ToxiproxyAPIController.PROXIES_ENDPOINT;
import static toxiproxy.api.controller.ToxiproxyAPIController.SERVICE_VERSION_ENDPOINT;

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
    doReturn(clientProxies).when(toxiproxyAPIService).getProxies();

    String sProxies = mockMvc.perform(get("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT)
                                          .secure(true))
                             .andExpect(status().isOk())
                             .andReturn()
                             .getResponse()
                             .getContentAsString();

    Set<ClientProxy> returnedProxies = Sets.newSet(objectMapper.readValue(sProxies, ClientProxy[].class));
    assertNotNull(returnedProxies);
    assertEquals(1, returnedProxies.size());
    assertTrue(returnedProxies.contains(clientProxy));
  }

  @Test
  void getProxy() throws Exception {
    String proxyName = UUID.randomUUID().toString();
    ClientProxy foundProxy = ClientProxyBuilder.builder().build();
    doReturn(foundProxy).when(toxiproxyAPIService).getProxy(proxyName);

    String sProxy = mockMvc.perform(get("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT + "/{proxyName}", proxyName)
                                        .secure(true))
                           .andExpect(status().isOk())
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

    ClientProxy returnedClientProxy = objectMapper.readValue(sProxy, ClientProxy.class);
    assertNotNull(returnedClientProxy);
    assertEquals(foundProxy, returnedClientProxy);
  }

  @Test
  void getProxyWhenNotFound() throws Exception {
    String proxyName = UUID.randomUUID().toString();
    doReturn(null).when(toxiproxyAPIService).getProxy(proxyName);

    mockMvc.perform(get("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT + "/{proxyName}", proxyName)
                        .secure(true))
           .andExpect(status().isNotFound())
           .andReturn();

  }

  @Test
  void createProxy() throws Exception {
    ClientProxy clientProxy = ClientProxyBuilder.builder().build();
    ClientProxy newClientProxy = ClientProxyBuilder.builder().build();
    doReturn(newClientProxy).when(toxiproxyAPIService).createProxy(eq(clientProxy));

    String sReturnedProxy = mockMvc.perform(post("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT)
                                                .content(objectMapper.writeValueAsString(clientProxy))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .secure(true))
                                   .andExpect(status().isOk())
                                   .andReturn()
                                   .getResponse()
                                   .getContentAsString();

    ClientProxy returnedClientProxy = objectMapper.readValue(sReturnedProxy, ClientProxy.class);
    assertNotNull(returnedClientProxy);
    assertEquals(newClientProxy, returnedClientProxy);
  }

  @Test
  void getServiceVersion() throws Exception {
    String serviceVersion = UUID.randomUUID().toString();
    doReturn(serviceVersion).when(toxiproxyAPIService).getServiceVersion();

    String returnedServiceVersion = mockMvc.perform(get("/" + API_ENDPOINT + "/" + SERVICE_VERSION_ENDPOINT)
                                                        .secure(true))
                                           .andExpect(status().isOk())
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString();

    assertNotNull(returnedServiceVersion);
    assertEquals(serviceVersion, returnedServiceVersion);
  }
}
