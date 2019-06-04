package toxiproxy.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import toxiproxy.client.ToxiproxyClient;
import toxiproxy.client.dto.ClientProxy;
import toxiproxy.client.dto.ClientToxic;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

  @Test
  void getProxy() {
    String proxyName = UUID.randomUUID().toString();
    ClientProxy foundProxy = mock(ClientProxy.class);
    when(toxiproxyClient.getProxy(proxyName)).thenReturn(foundProxy);

    ClientProxy returnedProxy = toxiproxyAPIService.getProxy(proxyName);

    assertNotNull(returnedProxy);
    assertEquals(foundProxy, returnedProxy);
  }

  @Test
  void createProxy() {
    ClientProxy clientProxy = mock(ClientProxy.class);
    ClientProxy newProxy = mock(ClientProxy.class);
    when(toxiproxyClient.createProxy(clientProxy)).thenReturn(newProxy);

    ClientProxy returnedProxy = toxiproxyAPIService.createProxy(clientProxy);

    assertNotNull(returnedProxy);
    assertEquals(newProxy, returnedProxy);
  }

  @Test
  void updateProxy() {
    ClientProxy clientProxy = mock(ClientProxy.class);
    ClientProxy updatedClientProxy = mock(ClientProxy.class);
    when(toxiproxyClient.updateProxy(clientProxy)).thenReturn(updatedClientProxy);

    ClientProxy returnedClientProxy = toxiproxyAPIService.updateProxy(clientProxy);

    assertNotNull(returnedClientProxy);
    assertEquals(updatedClientProxy, returnedClientProxy);
  }

  @Test
  void deleteProxy() {
    String proxyName = UUID.randomUUID().toString();

    toxiproxyAPIService.deleteProxy(proxyName);

    verify(toxiproxyClient, times(1)).deleteProxy(proxyName);
  }

  @Test
  void addToxic() {
    String proxyName = UUID.randomUUID().toString();
    ClientToxic clientToxic = mock(ClientToxic.class);
    ClientToxic newClientToxic = mock(ClientToxic.class);
    when(toxiproxyClient.addToxic(proxyName, clientToxic)).thenReturn(newClientToxic);

    ClientToxic returnedClientToxic = toxiproxyAPIService.addToxic(proxyName, clientToxic);

    assertNotNull(returnedClientToxic);
    assertEquals(newClientToxic, returnedClientToxic);
  }

  @Test
  void getServiceVersion() {
    String serviceVersion = UUID.randomUUID().toString();
    when(toxiproxyClient.getVersion()).thenReturn(serviceVersion);

    String returnedServiceVersion = toxiproxyAPIService.getServiceVersion();

    assertNotNull(returnedServiceVersion);
    assertEquals(serviceVersion, returnedServiceVersion);
  }
}
