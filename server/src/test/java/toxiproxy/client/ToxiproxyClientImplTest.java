package toxiproxy.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToxiproxyClientImplTest {

  @InjectMocks private ToxiproxyClientImpl toxiproxyClient = spy(ToxiproxyClientImpl.class);
  @Mock private RestTemplate restTemplate;

  @Test
  void getProxiesRawString() {
    String host = UUID.randomUUID().toString() +":8474";
    String content = UUID.randomUUID().toString();
    doReturn(host).when(toxiproxyClient).getHost();
    when(restTemplate.getForObject(host + "/proxies", String.class)).thenReturn(content);

    String proxiesRawString = toxiproxyClient.getProxiesRawString();

    assertNotNull(proxiesRawString);
  }

  @Test
  void getProxiesRawStringWhenNoProxies() {
    String host = UUID.randomUUID().toString() +":8474";
    String content = "{}";
    doReturn(host).when(toxiproxyClient).getHost();
    when(restTemplate.getForObject(host + "/proxies", String.class)).thenReturn(content);

    String proxiesRawString = toxiproxyClient.getProxiesRawString();

    assertNull(proxiesRawString);
  }

  @Test
  void getProxiesRawStringWhenNoProxiesWithUnexpectedSpaces() {
    String host = UUID.randomUUID().toString() +":8474";
    String content = "  {  }      ";
    doReturn(host).when(toxiproxyClient).getHost();
    when(restTemplate.getForObject(host + "/proxies", String.class)).thenReturn(content);

    String proxiesRawString = toxiproxyClient.getProxiesRawString();

    assertNull(proxiesRawString);
  }
}
