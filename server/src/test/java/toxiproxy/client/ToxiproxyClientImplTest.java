package toxiproxy.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import toxiproxy.client.dto.ClientPopulateResponse;
import toxiproxy.client.dto.ClientProxy;
import toxiproxy.client.dto.ClientToxic;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToxiproxyClientImplTest {

  @InjectMocks private ToxiproxyClientImpl toxiproxyClient = spy(ToxiproxyClientImpl.class);
  @Mock private RestTemplate toxiproxyClientRestTemplate;

  private String url;
  private String hostname;

  @BeforeEach
  void setUp() {
    hostname = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5);
    url = "http://" + hostname + ":8474";
    doReturn(url).when(toxiproxyClient).getURL();
  }

  @Test
  void populate() {
    Set<ClientProxy> clientProxies = Sets.newSet();
    clientProxies.add(mock(ClientProxy.class));

    Set<ClientProxy> clientProxiesReturned = Sets.newSet();
    ClientPopulateResponse clientPopulateResponse = new ClientPopulateResponse();
    clientPopulateResponse.setProxies(clientProxiesReturned);

    when(toxiproxyClientRestTemplate.postForObject(url + "/populate", clientProxies, ClientPopulateResponse.class)).thenReturn(clientPopulateResponse);

    Set<ClientProxy> populatedClientProxies = toxiproxyClient.populate(clientProxies);

    assertNotNull(populatedClientProxies);
    assertEquals(clientProxiesReturned, populatedClientProxies);
  }

  @Test
  void populateWhenNullClientProxies() {
    Set<ClientProxy> populatedClientProxies = toxiproxyClient.populate(null);

    assertNull(populatedClientProxies);
    verify(toxiproxyClientRestTemplate, never()).postForObject(anyString(), any(), eq(ClientPopulateResponse.class));
  }

  @Test
  void populateWhenEmptyClientProxies() {
    Set<ClientProxy> populatedClientProxies = toxiproxyClient.populate(new HashSet<>());

    assertNull(populatedClientProxies);
    verify(toxiproxyClientRestTemplate, never()).postForObject(anyString(), any(), eq(ClientPopulateResponse.class));
  }

  @Test
  void getProxies() {
    ClientProxy clientProxy = mock(ClientProxy.class);
    HashMap<String, ClientProxy> proxyHashMap = new HashMap<>();
    proxyHashMap.put("", clientProxy);

    ResponseEntity<HashMap<String, ClientProxy>> exchange = mock(ResponseEntity.class);
    when(exchange.getBody()).thenReturn(proxyHashMap);

    when(toxiproxyClientRestTemplate.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(exchange);

    Set<ClientProxy> proxies = toxiproxyClient.getProxies();

    assertNotNull(proxies);
    assertEquals(proxyHashMap.size(), proxies.size());
    assertTrue(proxies.contains(clientProxy));

    ArgumentCaptor<RequestEntity> requestEntityArgumentCaptor = ArgumentCaptor.forClass(RequestEntity.class);
    verify(toxiproxyClientRestTemplate, times(1)).exchange(requestEntityArgumentCaptor.capture(), any(ParameterizedTypeReference.class));
    URI url = requestEntityArgumentCaptor.getValue().getUrl();
    assertEquals(hostname, url.getHost());
    assertEquals("/proxies", url.getPath());
  }

  @Test
  void getProxiesWhenEmpty() {
    ResponseEntity<HashMap<String, ClientProxy>> exchange = mock(ResponseEntity.class);
    when(exchange.getBody()).thenReturn(new HashMap<>());

    when(toxiproxyClientRestTemplate.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(exchange);

    Set<ClientProxy> proxies = toxiproxyClient.getProxies();

    assertNull(proxies);

    ArgumentCaptor<RequestEntity> requestEntityArgumentCaptor = ArgumentCaptor.forClass(RequestEntity.class);
    verify(toxiproxyClientRestTemplate, times(1)).exchange(requestEntityArgumentCaptor.capture(), any(ParameterizedTypeReference.class));
    URI url = requestEntityArgumentCaptor.getValue().getUrl();
    assertEquals(hostname, url.getHost());
    assertEquals("/proxies", url.getPath());
  }

  @Test
  void getProxiesWhenNull() {
    ResponseEntity<HashMap<String, ClientProxy>> exchange = mock(ResponseEntity.class);
    when(exchange.getBody()).thenReturn(null);

    when(toxiproxyClientRestTemplate.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(exchange);

    Set<ClientProxy> proxies = toxiproxyClient.getProxies();

    assertNull(proxies);

    ArgumentCaptor<RequestEntity> requestEntityArgumentCaptor = ArgumentCaptor.forClass(RequestEntity.class);
    verify(toxiproxyClientRestTemplate, times(1)).exchange(requestEntityArgumentCaptor.capture(), any(ParameterizedTypeReference.class));
    URI url = requestEntityArgumentCaptor.getValue().getUrl();
    assertEquals(hostname, url.getHost());
    assertEquals("/proxies", url.getPath());
  }

  @Test
  void getProxy() {
    String proxyName = UUID.randomUUID().toString();
    ClientProxy responseProxy = mock(ClientProxy.class);
    when(toxiproxyClientRestTemplate.getForObject(url + "/proxies/{proxy-name}", ClientProxy.class, proxyName)).thenReturn(responseProxy);

    ClientProxy proxy = toxiproxyClient.getProxy(proxyName);

    assertNotNull(proxy);
    assertEquals(responseProxy, proxy);
  }

  @Test
  void getProxyWhenNotFound() {
    String proxyName = UUID.randomUUID().toString();
    when(toxiproxyClientRestTemplate.getForObject(url + "/proxies/{proxy-name}", ClientProxy.class, proxyName))
        .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, null, null, null, null));

    ClientProxy proxy = toxiproxyClient.getProxy(proxyName);

    assertNull(proxy);
  }

  @Test
  void getProxyWhenNullName() {
    String name = null;

    ClientProxy proxy = toxiproxyClient.getProxy(null);

    assertNull(proxy);
    verify(toxiproxyClientRestTemplate, never()).getForObject(anyString(), eq(ClientProxy.class), eq(name));
  }

  @Test
  void createProxy() {
    ClientProxy responseProxy = mock(ClientProxy.class);
    ClientProxy newProxy = mock(ClientProxy.class);
    when(toxiproxyClientRestTemplate.postForObject(url + "/proxies", newProxy, ClientProxy.class)).thenReturn(responseProxy);

    ClientProxy proxy = toxiproxyClient.createProxy(newProxy);

    assertNotNull(proxy);
    assertEquals(responseProxy, proxy);
  }

  @Test
  void createProxyWhenNullNewProxy() {
    ClientProxy proxy = toxiproxyClient.createProxy(null);

    assertNull(proxy);
    verify(toxiproxyClientRestTemplate, never()).postForObject(anyString(), any(), eq(ClientProxy.class));
  }

  @Test
  void updateProxy() {
    ClientProxy proxy = mock(ClientProxy.class);
    when(proxy.getName()).thenReturn(UUID.randomUUID().toString());
    ClientProxy updatedProxy = mock(ClientProxy.class);
    when(toxiproxyClientRestTemplate.postForObject(url + "/proxies/{proxy-name}", proxy, ClientProxy.class, proxy.getName()))
        .thenReturn(updatedProxy);

    ClientProxy returnedProxy = toxiproxyClient.updateProxy(proxy);

    assertNotNull(returnedProxy);
    assertEquals(updatedProxy, returnedProxy);
  }

  @Test
  void updateProxyWhenNullProxy() {
    ClientProxy returnedProxy = toxiproxyClient.updateProxy(null);

    assertNull(returnedProxy);
    verify(toxiproxyClientRestTemplate, never()).postForObject(anyString(), eq(null), eq(ClientProxy.class), anyString());
  }

  @Test
  void updateProxyWhenNullProxyName() {
    String proxyName = null;
    ClientProxy proxy = mock(ClientProxy.class);
    when(proxy.getName()).thenReturn(proxyName);

    ClientProxy returnedProxy = toxiproxyClient.updateProxy(proxy);

    assertNull(returnedProxy);
    verify(toxiproxyClientRestTemplate, never()).postForObject(anyString(), eq(proxy), eq(ClientProxy.class), eq(proxyName));
  }

  @Test
  void deleteProxy() {
    String proxyName = UUID.randomUUID().toString();

    toxiproxyClient.deleteProxy(proxyName);

    verify(toxiproxyClientRestTemplate, times(1)).delete(url + "/proxies/{proxy-name}", proxyName);
  }

  @Test
  void deleteProxyWhenNotFound() {
    String proxyName = UUID.randomUUID().toString();
    doThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, null, null, null, null))
        .when(toxiproxyClientRestTemplate).delete(url + "/proxies/{proxy-name}", proxyName);

    assertDoesNotThrow(() -> toxiproxyClient.deleteProxy(proxyName));
  }

  @Test
  void deleteProxyWhenNullName() {
    String name = null;

    toxiproxyClient.deleteProxy(name);

    verify(toxiproxyClientRestTemplate, never()).delete(anyString(), eq(name));
  }

  @Test
  void deleteAllProxies() {
    ClientProxy clientProxy1 = mock(ClientProxy.class);
    when(clientProxy1.getName()).thenReturn(UUID.randomUUID().toString());
    ClientProxy clientProxy2 = mock(ClientProxy.class);
    when(clientProxy2.getName()).thenReturn(UUID.randomUUID().toString());

    Set<ClientProxy> proxies = Sets.newSet(clientProxy1, clientProxy2);
    doReturn(proxies).when(toxiproxyClient).getProxies();

    toxiproxyClient.deleteAllProxies();

    verify(toxiproxyClient, times(1)).deleteProxy(clientProxy1.getName());
    verify(toxiproxyClient, times(1)).deleteProxy(clientProxy2.getName());
  }

  @Test
  void deleteAllProxiesWhenNone() {
    doReturn(null).when(toxiproxyClient).getProxies();

    toxiproxyClient.deleteAllProxies();

    verify(toxiproxyClient, never()).deleteProxy(any());
  }

  @Test
  void addToxic() {
    String proxyName = UUID.randomUUID().toString();
    ClientToxic toxic = mock(ClientToxic.class);
    ClientToxic newToxic = mock(ClientToxic.class);
    when(toxiproxyClientRestTemplate.postForObject(url + "/proxies/{proxy-name}/toxics", toxic, ClientToxic.class, proxyName)).thenReturn(newToxic);

    ClientToxic returnedToxic = toxiproxyClient.addToxic(proxyName, toxic);

    assertNotNull(returnedToxic);
    assertEquals(newToxic, returnedToxic);
  }

  @Test
  void addToxicWhenProxyNameNull() {
    String proxyName = null;
    ClientToxic toxic = mock(ClientToxic.class);

    ClientToxic returnedToxic = toxiproxyClient.addToxic(proxyName, toxic);

    assertNull(returnedToxic);
    verify(toxiproxyClientRestTemplate, never()).postForObject(anyString(), eq(toxic), eq(ClientToxic.class), eq(proxyName));
  }

  @Test
  void addToxicWhenToxicNull() {
    String proxyName = UUID.randomUUID().toString();
    ClientToxic toxic = null;

    ClientToxic returnedToxic = toxiproxyClient.addToxic(proxyName, toxic);

    assertNull(returnedToxic);
    verify(toxiproxyClientRestTemplate, never()).postForObject(anyString(), eq(toxic), eq(ClientToxic.class), eq(proxyName));
  }

  @Test
  void updateToxic() {
    String proxyName = UUID.randomUUID().toString();
    ClientToxic toxic = mock(ClientToxic.class);
    when(toxic.getName()).thenReturn(UUID.randomUUID().toString());
    ClientToxic updatedToxic = mock(ClientToxic.class);
    when(toxiproxyClientRestTemplate.postForObject(url + "/proxies/{proxy-name}/toxics/{toxic-name}",
                                                   toxic,
                                                   ClientToxic.class,
                                                   proxyName,
                                                   toxic.getName()))
        .thenReturn(updatedToxic);

    ClientToxic returnedToxic = toxiproxyClient.updateToxic(proxyName, toxic);

    assertNotNull(returnedToxic);
    assertEquals(updatedToxic, returnedToxic);
  }

  @Test
  void updateToxicWhenProxyNameNull() {
    String proxyName = null;
    ClientToxic toxic = mock(ClientToxic.class);

    ClientToxic returnedToxic = toxiproxyClient.updateToxic(proxyName, toxic);

    assertNull(returnedToxic);
    verify(toxiproxyClientRestTemplate, never()).postForObject(anyString(), eq(toxic), eq(ClientToxic.class), eq(proxyName), anyString());
  }

  @Test
  void updateToxicWhenToxicNull() {
    String proxyName = UUID.randomUUID().toString();
    ClientToxic toxic = null;

    ClientToxic returnedToxic = toxiproxyClient.updateToxic(proxyName, toxic);

    assertNull(returnedToxic);
    verify(toxiproxyClientRestTemplate, never()).postForObject(anyString(), eq(toxic), eq(ClientToxic.class), eq(proxyName), anyString());
  }

  @Test
  void updateToxicWhenToxicNameNull() {
    String proxyName = UUID.randomUUID().toString();
    String toxicName = null;
    ClientToxic toxic = mock(ClientToxic.class);
    when(toxic.getName()).thenReturn(toxicName);

    ClientToxic returnedToxic = toxiproxyClient.updateToxic(proxyName, toxic);

    assertNull(returnedToxic);
    verify(toxiproxyClientRestTemplate, never()).postForObject(anyString(), eq(toxic), eq(ClientToxic.class), eq(proxyName), eq(toxicName));
  }

  @Test
  void deleteToxic() {
    String proxyName = UUID.randomUUID().toString();
    String toxicName = UUID.randomUUID().toString();

    toxiproxyClient.deleteToxic(proxyName, toxicName);

    verify(toxiproxyClientRestTemplate, times(1)).delete(url + "/proxies/{proxy-name}/toxics/{toxic-name}", proxyName, toxicName);
  }

  @Test
  void deleteToxicWhenNotFound() {
    String proxyName = UUID.randomUUID().toString();
    String toxicName = UUID.randomUUID().toString();
    doThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, null, null, null, null))
        .when(toxiproxyClientRestTemplate).delete(url + "/proxies/{proxy-name}/toxics/{toxic-name}", proxyName, toxicName);

    assertDoesNotThrow(() -> toxiproxyClient.deleteToxic(proxyName, toxicName));
  }

  @Test
  void deleteToxicWhenProxyNameNull() {
    String proxyName = null;
    String toxicName = UUID.randomUUID().toString();

    toxiproxyClient.deleteToxic(proxyName, toxicName);

    verify(toxiproxyClientRestTemplate, never()).delete(url + "/proxies/{proxy-name}/toxics/{toxic-name}", proxyName, toxicName);
  }

  @Test
  void deleteToxicWhenToxicNameNull() {
    String proxyName = UUID.randomUUID().toString();
    String toxicName = null;

    toxiproxyClient.deleteToxic(proxyName, toxicName);

    verify(toxiproxyClientRestTemplate, never()).delete(url + "/proxies/{proxy-name}/toxics/{toxic-name}", proxyName, toxicName);
  }

  @Test
  void version() {
    String version = UUID.randomUUID().toString();
    when(toxiproxyClientRestTemplate.getForObject(url + "/version", String.class)).thenReturn(version);

    assertEquals(version, toxiproxyClient.getVersion());
  }

  @Test
  void getURL() {
    doCallRealMethod().when(toxiproxyClient).getURL();
    assertNull(toxiproxyClient.getURL());
  }
}
