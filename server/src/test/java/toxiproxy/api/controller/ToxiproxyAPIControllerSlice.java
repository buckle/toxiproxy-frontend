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
import org.springframework.web.client.HttpClientErrorException;
import toxiproxy.api.service.ToxiproxyAPIService;
import toxiproxy.client.dto.ClientProxy;
import toxiproxy.client.dto.ClientProxyBuilder;
import toxiproxy.client.dto.ClientToxic;
import toxiproxy.client.dto.ClientToxicBuilder;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static toxiproxy.api.controller.ToxiproxyAPIController.API_ENDPOINT;
import static toxiproxy.api.controller.ToxiproxyAPIController.PROXIES_ENDPOINT;
import static toxiproxy.api.controller.ToxiproxyAPIController.SERVICE_VERSION_ENDPOINT;
import static toxiproxy.api.controller.ToxiproxyAPIController.TOXICS_ENDPOINT;

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
    doThrow(HttpClientErrorException.NotFound.class).when(toxiproxyAPIService).getProxy(proxyName);

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
  void updateProxy() throws Exception {
    ClientProxy clientProxy = ClientProxyBuilder.builder().build();
    ClientProxy updatedClientProxy = ClientProxyBuilder.builder().build();
    doReturn(updatedClientProxy).when(toxiproxyAPIService).updateProxy(clientProxy);

    String sReturnedProxy = mockMvc.perform(post("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT + "/{proxyName}", clientProxy.getName())
                                                .content(objectMapper.writeValueAsString(clientProxy))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .secure(true))
                                   .andExpect(status().isOk())
                                   .andReturn()
                                   .getResponse()
                                   .getContentAsString();

    ClientProxy returnedClientProxy = objectMapper.readValue(sReturnedProxy, ClientProxy.class);
    assertNotNull(returnedClientProxy);
    assertEquals(updatedClientProxy, returnedClientProxy);
  }

  @Test
  void updateProxyWhenNotFound() throws Exception {
    ClientProxy clientProxy = ClientProxyBuilder.builder().build();
    doThrow(HttpClientErrorException.NotFound.class).when(toxiproxyAPIService).updateProxy(clientProxy);

    mockMvc.perform(post("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT + "/{proxyName}", clientProxy.getName())
                        .content(objectMapper.writeValueAsString(clientProxy))
                        .contentType(MediaType.APPLICATION_JSON)
                        .secure(true))
           .andExpect(status().isNotFound())
           .andReturn();
  }

  @Test
  void deleteProxy() throws Exception {
    String proxyName = UUID.randomUUID().toString();
    doNothing().when(toxiproxyAPIService).deleteProxy(proxyName);

    mockMvc.perform(delete("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT + "/{proxyName}", proxyName)
                        .secure(true))
           .andExpect(status().isOk())
           .andReturn();

    verify(toxiproxyAPIService, times(1)).deleteProxy(proxyName);
  }

  @Test
  void deleteProxyWhenNotFound() throws Exception {
    String proxyName = UUID.randomUUID().toString();
    doThrow(HttpClientErrorException.NotFound.class).when(toxiproxyAPIService).deleteProxy(proxyName);

    mockMvc.perform(delete("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT + "/{proxyName}", proxyName)
                        .secure(true))
           .andExpect(status().isNotFound())
           .andReturn();

    verify(toxiproxyAPIService, times(1)).deleteProxy(proxyName);
  }

  @Test
  void addToxic() throws Exception {
    String proxyName = UUID.randomUUID().toString();
    ClientToxic clientToxic = ClientToxicBuilder.builder().build();
    ClientToxic newClientToxic = ClientToxicBuilder.builder().build();
    doReturn(newClientToxic).when(toxiproxyAPIService).addToxic(proxyName, clientToxic);

    String sReturnedClientToxic = mockMvc.perform(post("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT + "/{proxyName}/" + TOXICS_ENDPOINT, proxyName)
                                                      .content(objectMapper.writeValueAsString(clientToxic))
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .secure(true))
                                         .andExpect(status().isOk())
                                         .andReturn()
                                         .getResponse()
                                         .getContentAsString();

    ClientToxic returnedClientToxic = objectMapper.readValue(sReturnedClientToxic, ClientToxic.class);
    assertNotNull(returnedClientToxic);
    assertEquals(newClientToxic, returnedClientToxic);
  }

  @Test
  void addToxicWhenProxyNotFound() throws Exception {
    String proxyName = UUID.randomUUID().toString();
    ClientToxic clientToxic = ClientToxicBuilder.builder().build();
    doThrow(HttpClientErrorException.NotFound.class).when(toxiproxyAPIService).addToxic(proxyName, clientToxic);

    mockMvc.perform(post("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT + "/{proxyName}/" + TOXICS_ENDPOINT, proxyName)
                        .content(objectMapper.writeValueAsString(clientToxic))
                        .contentType(MediaType.APPLICATION_JSON)
                        .secure(true))
           .andExpect(status().isNotFound())
           .andReturn();
  }

  @Test
  void updateToxic() throws Exception {
    String proxyName = UUID.randomUUID().toString();
    ClientToxic clientToxic = ClientToxicBuilder.builder().build();
    ClientToxic updatedClientToxic = ClientToxicBuilder.builder().build();
    doReturn(updatedClientToxic).when(toxiproxyAPIService).updateToxic(proxyName, clientToxic);

    String sReturnedClientToxic = mockMvc.perform(
        post("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT + "/{proxyName}/" + TOXICS_ENDPOINT + "/{toxicName}", proxyName, clientToxic.getName())
            .content(objectMapper.writeValueAsString(clientToxic))
            .contentType(MediaType.APPLICATION_JSON)
            .secure(true))
                                         .andExpect(status().isOk())
                                         .andReturn()
                                         .getResponse()
                                         .getContentAsString();

    ClientToxic returnedClientToxic = objectMapper.readValue(sReturnedClientToxic, ClientToxic.class);
    assertNotNull(returnedClientToxic);
    assertEquals(updatedClientToxic, returnedClientToxic);
  }

  @Test
  void updateToxicWhenSomethingNotFound() throws Exception {
    String proxyName = UUID.randomUUID().toString();
    ClientToxic clientToxic = ClientToxicBuilder.builder().build();
    doThrow(HttpClientErrorException.NotFound.class).when(toxiproxyAPIService).updateToxic(proxyName, clientToxic);

    mockMvc.perform(
        post("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT + "/{proxyName}/" + TOXICS_ENDPOINT + "/{toxicName}", proxyName, clientToxic.getName())
            .content(objectMapper.writeValueAsString(clientToxic))
            .contentType(MediaType.APPLICATION_JSON)
            .secure(true))
           .andExpect(status().isNotFound())
           .andReturn();
  }

  @Test
  void deleteToxic() throws Exception {
    String proxyName = UUID.randomUUID().toString();
    String toxicName = UUID.randomUUID().toString();
    doNothing().when(toxiproxyAPIService).deleteToxic(proxyName, toxicName);

    mockMvc.perform(
        delete("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT + "/{proxyName}/" + TOXICS_ENDPOINT + "/{toxicName}", proxyName, toxicName)
            .secure(true))
           .andExpect(status().isOk())
           .andReturn();

    verify(toxiproxyAPIService, times(1)).deleteToxic(proxyName, toxicName);
  }

  @Test
  void deleteToxicWhenSomethingNotFound() throws Exception {
    String proxyName = UUID.randomUUID().toString();
    String toxicName = UUID.randomUUID().toString();
    doThrow(HttpClientErrorException.NotFound.class).when(toxiproxyAPIService).deleteToxic(proxyName, toxicName);

    mockMvc.perform(
        delete("/" + API_ENDPOINT + "/" + PROXIES_ENDPOINT + "/{proxyName}/" + TOXICS_ENDPOINT + "/{toxicName}", proxyName, toxicName)
            .secure(true))
           .andExpect(status().isNotFound())
           .andReturn();

    verify(toxiproxyAPIService, times(1)).deleteToxic(proxyName, toxicName);
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
