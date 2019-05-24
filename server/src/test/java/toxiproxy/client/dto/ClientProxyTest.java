package toxiproxy.client.dto;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientProxyTest {

  @Test
  void equalsAndHashWhenObjectsSame() {
    ClientProxy clientProxy = new ClientProxy();

    assertTrue(clientProxy.equals(clientProxy));
    assertEquals(clientProxy.hashCode(), clientProxy.hashCode());
  }

  @Test
  void equalsAndHashWhenDataSame() {
    ClientProxy clientProxy1 = ClientProxyBuilder.builder().build();
    ClientProxy clientProxy2 = createClone(clientProxy1);

    assertTrue(clientProxy1.equals(clientProxy2));
    assertEquals(clientProxy1.hashCode(), clientProxy2.hashCode());
  }

  @Test
  void equalsAndHashWhenNameDifferent() {
    ClientProxy clientProxy1 = ClientProxyBuilder.builder().build();
    ClientProxy clientProxy2 = createClone(clientProxy1);
    clientProxy1.setName(UUID.randomUUID().toString());

    assertFalse(clientProxy1.equals(clientProxy2));
    assertNotEquals(clientProxy1.hashCode(), clientProxy2.hashCode());
  }

  @Test
  void equalsAndHashWhenListenDifferent() {
    ClientProxy clientProxy1 = ClientProxyBuilder.builder().build();
    ClientProxy clientProxy2 = createClone(clientProxy1);
    clientProxy1.setListen(UUID.randomUUID().toString());

    assertFalse(clientProxy1.equals(clientProxy2));
    assertNotEquals(clientProxy1.hashCode(), clientProxy2.hashCode());
  }

  @Test
  void equalsAndHashWhenUpstreamDifferent() {
    ClientProxy clientProxy1 = ClientProxyBuilder.builder().build();
    ClientProxy clientProxy2 = createClone(clientProxy1);
    clientProxy1.setUpstream(UUID.randomUUID().toString());

    assertFalse(clientProxy1.equals(clientProxy2));
    assertNotEquals(clientProxy1.hashCode(), clientProxy2.hashCode());
  }

  @Test
  void equalsAndHashWhenEnabledDifferent() {
    ClientProxy clientProxy1 = ClientProxyBuilder.builder().build();
    ClientProxy clientProxy2 = createClone(clientProxy1);
    clientProxy1.setEnabled(true);
    clientProxy2.setEnabled(false);

    assertFalse(clientProxy1.equals(clientProxy2));
    assertNotEquals(clientProxy1.hashCode(), clientProxy2.hashCode());
  }

  private ClientProxy createClone(ClientProxy clientProxy) {
    ClientProxy clientProxyClone = new ClientProxy();
    clientProxyClone.setName(clientProxy.getName());
    clientProxyClone.setListen(clientProxy.getListen());
    clientProxyClone.setUpstream(clientProxy.getUpstream());
    clientProxyClone.setEnabled(clientProxy.isEnabled());
    return clientProxyClone;
  }
}
