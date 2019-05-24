package toxiproxy.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toxiproxy.client.dto.ClientProxy;
import toxiproxy.client.dto.ClientProxyBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ToxiproxyClientImplIT
 * <p>
 * Used to rough out the API calls and mocking assumptions for the Toxiproxy client. Keeping it around in case we want to debug or actually startup a
 * Toxiproxy docker image as part of CI in the future.
 */
@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ToxiproxyClientImplIT {

  @Autowired private ToxiproxyClient toxiproxyClient;

  @BeforeEach
  void setUp() {
    toxiproxyClient.deleteAllProxies();
  }

  @AfterEach
  void tearDown() {
    toxiproxyClient.deleteAllProxies();
  }

  @Test
  void populate() {
    Set<ClientProxy> clientProxies = new HashSet<>();
    clientProxies.add(ClientProxyBuilder.builder().listen("localhost:5556").build());

    Set<ClientProxy> populatedProxies = toxiproxyClient.populate(clientProxies);

    assertNotNull(populatedProxies);
  }

  @Test
  void getProxies() {
    ClientProxy newProxy = ClientProxyBuilder.builder().listen("localhost:5556").build();
    toxiproxyClient.createProxy(newProxy);

    Set<ClientProxy> proxies = toxiproxyClient.getProxies();

    assertNotNull(proxies);
    assertFalse(proxies.isEmpty());
    assertNotNull(proxies.stream()
                         .filter(clientProxy -> clientProxy.getName().equals(newProxy.getName()))
                         .findFirst()
                         .orElse(null));
  }

  @Test
  void getProxy() {
    ClientProxy newProxy = ClientProxyBuilder.builder().listen("localhost:5556").build();
    toxiproxyClient.createProxy(newProxy);

    ClientProxy proxy = toxiproxyClient.getProxy(newProxy.getName());

    assertNotNull(proxy);
    assertEquals(newProxy.getName(), proxy.getName());
  }

  @Test
  void createProxy() {
    ClientProxy newProxy = ClientProxyBuilder.builder().listen("localhost:5556").build();

    ClientProxy proxy = toxiproxyClient.createProxy(newProxy);

    assertNotNull(proxy);
    assertEquals(newProxy.getName(), proxy.getName());
  }

  @Test
  void deleteProxy() {
    ClientProxy newProxy = ClientProxyBuilder.builder().listen("localhost:5556").build();
    ClientProxy proxy = toxiproxyClient.createProxy(newProxy);
    assertNotNull(proxy);

    toxiproxyClient.deleteProxy(newProxy.getName());

    assertNull(toxiproxyClient.getProxy(newProxy.getName()));
  }

  @Test
  void deleteAllProxies() {
    ClientProxy proxy1 = toxiproxyClient.createProxy(ClientProxyBuilder.builder().listen("localhost:5556").build());
    ClientProxy proxy2 = toxiproxyClient.createProxy(ClientProxyBuilder.builder().listen("localhost:5557").build());

    Set<ClientProxy> proxies = toxiproxyClient.getProxies();
    assertTrue(proxies.containsAll(Arrays.asList(proxy1, proxy2)));

    toxiproxyClient.deleteAllProxies();

    proxies = toxiproxyClient.getProxies();
    assertNull(proxies);
  }
}
