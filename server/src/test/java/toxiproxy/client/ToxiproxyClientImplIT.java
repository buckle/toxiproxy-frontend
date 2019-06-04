package toxiproxy.client;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Precision;
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
import toxiproxy.client.dto.ClientToxic;
import toxiproxy.client.dto.ClientToxicBuilder;

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
  void updateProxy() {
    ClientProxy newProxy = ClientProxyBuilder.builder().listen("localhost:5556").build();
    ClientProxy proxy = toxiproxyClient.createProxy(newProxy);
    proxy.setListen("127.0.0.1:5558");

    ClientProxy updatedProxy = toxiproxyClient.updateProxy(proxy);

    assertNotNull(updatedProxy);
    assertEquals(newProxy.getName(), updatedProxy.getName());
    assertEquals(newProxy.getUpstream(), updatedProxy.getUpstream());
    assertEquals(newProxy.isEnabled(), updatedProxy.isEnabled());
    assertEquals(proxy.getListen(), updatedProxy.getListen());
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

  @Test
  void addToxic() {
    ClientProxy proxy = toxiproxyClient.createProxy(ClientProxyBuilder.builder().listen("localhost:5556").build());
    ClientToxic toxic = ClientToxicBuilder.builder().build();

    ClientToxic clientToxic = toxiproxyClient.addToxic(proxy.getName(), toxic);

    assertNotNull(clientToxic);
    assertEquals(toxic.getName(), clientToxic.getName());

    ClientProxy updatedProxy = toxiproxyClient.getProxy(proxy.getName());
    assertTrue(updatedProxy.getToxics().contains(clientToxic));
  }

  @Test
  void updateToxic() {
    ClientProxy clientProxy = toxiproxyClient.createProxy(ClientProxyBuilder.builder().listen("localhost:5556").build());
    ClientToxic clientToxic = toxiproxyClient.addToxic(clientProxy.getName(), ClientToxicBuilder.builder()
                                                                                                .toxicity(RandomUtils.nextDouble(0, 1))
                                                                                                .build());
    clientToxic.setToxicity(Precision.round(RandomUtils.nextDouble(0, 1), 4));
    ClientToxic updatedClientToxic = toxiproxyClient.updateToxic(clientProxy.getName(), clientToxic);

    assertNotNull(updatedClientToxic);
    assertEquals(clientToxic.getToxicity(), updatedClientToxic.getToxicity());
  }

  @Test
  void deleteToxic() {
    ClientProxy clientProxy = toxiproxyClient.createProxy(ClientProxyBuilder.builder().listen("localhost:5556").build());
    ClientToxic clientToxic = toxiproxyClient.addToxic(clientProxy.getName(), ClientToxicBuilder.builder().build());
    clientProxy = toxiproxyClient.getProxy(clientProxy.getName());
    assertTrue(clientProxy.getToxics().contains(clientToxic));

    toxiproxyClient.deleteToxic(clientProxy.getName(), clientToxic.getName());

    clientProxy = toxiproxyClient.getProxy(clientProxy.getName());
    assertFalse(clientProxy.getToxics().contains(clientToxic));
  }

  @Test
  void getVersion() {
    String version = toxiproxyClient.getVersion();

    assertFalse(StringUtils.isEmpty(version));
    assertTrue(StringUtils.isAlphanumeric(version.replaceAll("-", "")));
  }
}
