package toxiproxy.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import toxiproxy.client.dto.ClientProxy;

import java.net.URI;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToxiproxyClientImplTest {

  @InjectMocks private ToxiproxyClientImpl toxiproxyClient = spy(ToxiproxyClientImpl.class);
  @Mock private RestTemplate restTemplate;

  private String url;
  private String hostname;

  @BeforeEach
  void setUp() {
    hostname = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5);
    url = "http://" + hostname + ":8474";
    doReturn(url).when(toxiproxyClient).getURL();
  }

  @Test
  void getProxies() {
    ClientProxy clientProxy = mock(ClientProxy.class);
    HashMap<String, ClientProxy> proxyHashMap = new HashMap<>();
    proxyHashMap.put("", clientProxy);

    ResponseEntity<HashMap<String, ClientProxy>> exchange = mock(ResponseEntity.class);
    when(exchange.getBody()).thenReturn(proxyHashMap);

    when(restTemplate.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(exchange);

    Set<ClientProxy> proxies = toxiproxyClient.getProxies();

    assertNotNull(proxies);
    assertEquals(proxyHashMap.size(), proxies.size());
    assertTrue(proxies.contains(clientProxy));

    ArgumentCaptor<RequestEntity> requestEntityArgumentCaptor = ArgumentCaptor.forClass(RequestEntity.class);
    verify(restTemplate, times(1)).exchange(requestEntityArgumentCaptor.capture(), any(ParameterizedTypeReference.class));
    URI url = requestEntityArgumentCaptor.getValue().getUrl();
    assertEquals(hostname, url.getHost());
    assertEquals("/proxies", url.getPath());
  }

  @Test
  void getProxiesWhenEmpty() {
    ResponseEntity<HashMap<String, ClientProxy>> exchange = mock(ResponseEntity.class);
    when(exchange.getBody()).thenReturn(new HashMap<>());

    when(restTemplate.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(exchange);

    Set<ClientProxy> proxies = toxiproxyClient.getProxies();

    assertNull(proxies);

    ArgumentCaptor<RequestEntity> requestEntityArgumentCaptor = ArgumentCaptor.forClass(RequestEntity.class);
    verify(restTemplate, times(1)).exchange(requestEntityArgumentCaptor.capture(), any(ParameterizedTypeReference.class));
    URI url = requestEntityArgumentCaptor.getValue().getUrl();
    assertEquals(hostname, url.getHost());
    assertEquals("/proxies", url.getPath());
  }

  @Test
  void getProxiesWhenNull() {
    ResponseEntity<HashMap<String, ClientProxy>> exchange = mock(ResponseEntity.class);
    when(exchange.getBody()).thenReturn(null);

    when(restTemplate.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(exchange);

    Set<ClientProxy> proxies = toxiproxyClient.getProxies();

    assertNull(proxies);

    ArgumentCaptor<RequestEntity> requestEntityArgumentCaptor = ArgumentCaptor.forClass(RequestEntity.class);
    verify(restTemplate, times(1)).exchange(requestEntityArgumentCaptor.capture(), any(ParameterizedTypeReference.class));
    URI url = requestEntityArgumentCaptor.getValue().getUrl();
    assertEquals(hostname, url.getHost());
    assertEquals("/proxies", url.getPath());
  }
}
