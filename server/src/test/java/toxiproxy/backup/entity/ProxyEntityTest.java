package toxiproxy.backup.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProxyEntityTest {

  @Test
  void equalsWhenNamesEqual() {
    ProxyEntity proxyEntity = new ProxyEntity();
    proxyEntity.setName(UUID.randomUUID().toString());

    ProxyEntity proxyEntity1 = new ProxyEntity();
    proxyEntity1.setName(proxyEntity.getName());

    assertTrue(proxyEntity.equals(proxyEntity1));
  }

  @Test
  void equalsWhenNamesDoNotEqual() {
    ProxyEntity proxyEntity = new ProxyEntity();
    proxyEntity.setName(UUID.randomUUID().toString());

    ProxyEntity proxyEntity1 = new ProxyEntity();
    proxyEntity1.setName(UUID.randomUUID().toString());

    assertFalse(proxyEntity.equals(proxyEntity1));
  }

  @Test
  void equalsWhenNamesBothNull() {
    ProxyEntity proxyEntity = new ProxyEntity();
    proxyEntity.setName(null);

    ProxyEntity proxyEntity1 = new ProxyEntity();
    proxyEntity1.setName(null);

    assertTrue(proxyEntity.equals(proxyEntity1));
  }

  @Test
  void equalsWhenOneNameNull() {
    ProxyEntity proxyEntity = new ProxyEntity();
    proxyEntity.setName(UUID.randomUUID().toString());

    ProxyEntity proxyEntity1 = new ProxyEntity();
    proxyEntity1.setName(null);

    assertFalse(proxyEntity.equals(proxyEntity1));
  }

  @Test
  void hashCodeWhenSameName() {
    ProxyEntity proxyEntity = new ProxyEntity();
    proxyEntity.setName(UUID.randomUUID().toString());

    ProxyEntity proxyEntity1 = new ProxyEntity();
    proxyEntity1.setName(proxyEntity.getName());

    assertEquals(proxyEntity.hashCode(), proxyEntity1.hashCode());
  }

  @Test
  void hashWhenDifferentName() {
    ProxyEntity proxyEntity = new ProxyEntity();
    proxyEntity.setName(UUID.randomUUID().toString());

    ProxyEntity proxyEntity1 = new ProxyEntity();
    proxyEntity1.setName(UUID.randomUUID().toString());

    assertNotEquals(proxyEntity.hashCode(), proxyEntity1.hashCode());
  }
}
