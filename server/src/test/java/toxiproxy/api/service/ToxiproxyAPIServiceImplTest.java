package toxiproxy.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import toxiproxy.client.ToxiproxyClient;
import toxiproxy.client.dto.ClientProxy;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToxiproxyAPIServiceImplTest {

  @InjectMocks private ToxiproxyAPIServiceImpl toxiproxyAPIService;
  @Mock private ToxiproxyClient toxiproxyClient;

  @Test
  void getProxies() {
    Set<ClientProxy> clientProxies = Sets.newSet();
    when(toxiproxyClient.getProxies()).thenReturn(clientProxies);

    Set<ClientProxy> proxies = toxiproxyAPIService.getProxies();

    assertNotNull(proxies);
    assertEquals(clientProxies, proxies);
  }
}
