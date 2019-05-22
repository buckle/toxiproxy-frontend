package toxiproxy.backup.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProxyEntityTest {

  @Test
  void equalsAndHashWhenObjectsSame() {
    ProxyEntity proxyEntity = new ProxyEntity();

    assertTrue(proxyEntity.equals(proxyEntity));
    assertEquals(proxyEntity.hashCode(), proxyEntity.hashCode());
  }

  @Test
  void equalsAndHashWhenHaveSameData() {
    ProxyEntity proxyEntity = ProxyEntityBuilder.builder().build();
    ProxyEntity proxyEntity1 = createClone(proxyEntity);

    assertTrue(proxyEntity.equals(proxyEntity1));
    assertEquals(proxyEntity.hashCode(), proxyEntity1.hashCode());
  }

  @Test
  void equalsAndHashWhenNameDifferent() {
    ProxyEntity proxyEntity = ProxyEntityBuilder.builder().build();
    ProxyEntity proxyEntity1 = createClone(proxyEntity);
    proxyEntity1.setName(UUID.randomUUID().toString());

    assertFalse(proxyEntity.equals(proxyEntity1));
    assertNotEquals(proxyEntity.hashCode(), proxyEntity1.hashCode());
  }

  @Test
  void equalsAndHashWhenListenDifferent() {
    ProxyEntity proxyEntity = ProxyEntityBuilder.builder().build();
    ProxyEntity proxyEntity1 = createClone(proxyEntity);
    proxyEntity1.setListen(UUID.randomUUID().toString());

    assertFalse(proxyEntity.equals(proxyEntity1));
    assertNotEquals(proxyEntity.hashCode(), proxyEntity1.hashCode());
  }

  @Test
  void equalsAndHashWhenUpstreamDifferent() {
    ProxyEntity proxyEntity = ProxyEntityBuilder.builder().build();
    ProxyEntity proxyEntity1 = createClone(proxyEntity);
    proxyEntity1.setUpstream(UUID.randomUUID().toString());

    assertFalse(proxyEntity.equals(proxyEntity1));
    assertNotEquals(proxyEntity.hashCode(), proxyEntity1.hashCode());
  }

  @Test
  void equalsAndHashWhenEnabledDifferent() {
    ProxyEntity proxyEntity = ProxyEntityBuilder.builder().build();
    proxyEntity.setEnabled(true);
    ProxyEntity proxyEntity1 = createClone(proxyEntity);
    proxyEntity1.setEnabled(false);

    assertFalse(proxyEntity.equals(proxyEntity1));
    assertNotEquals(proxyEntity.hashCode(), proxyEntity1.hashCode());
  }

  private ProxyEntity createClone(ProxyEntity proxyEntity) {
    ProxyEntity proxyEntityClone = new ProxyEntity();
    proxyEntityClone.setName(proxyEntity.getName());
    proxyEntityClone.setListen(proxyEntity.getListen());
    proxyEntityClone.setUpstream(proxyEntity.getUpstream());
    proxyEntityClone.setEnabled(proxyEntity.isEnabled());
    return proxyEntityClone;
  }
}
