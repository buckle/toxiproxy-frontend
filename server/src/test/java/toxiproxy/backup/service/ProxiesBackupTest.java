package toxiproxy.backup.service;

import org.junit.jupiter.api.Test;
import toxiproxy.backup.entity.ProxyEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProxiesBackupTest {

  @Test
  void equalsWhenHaveSameData() {
    Set<ProxyEntity> proxyEntities = createSetOfEntities();

    ProxiesBackup proxiesBackup = new ProxiesBackup();
    proxiesBackup.setData(proxyEntities);

    ProxiesBackup proxiesBackup1 = new ProxiesBackup();
    proxiesBackup1.setData(proxyEntities);

    assertTrue(proxiesBackup.equals(proxiesBackup1));
  }

  @Test
  void equalsWhenHaveDifferentData() {
    ProxiesBackup proxiesBackup = new ProxiesBackup();
    proxiesBackup.setData(createSetOfEntities());

    ProxiesBackup proxiesBackup1 = new ProxiesBackup();
    proxiesBackup1.setData(createSetOfEntities());

    assertFalse(proxiesBackup.equals(proxiesBackup1));
  }

  @Test
  void equalsWhenBothHaveNullData() {
    ProxiesBackup proxiesBackup = new ProxiesBackup();
    proxiesBackup.setData(null);

    ProxiesBackup proxiesBackup1 = new ProxiesBackup();
    proxiesBackup1.setData(null);

    assertTrue(proxiesBackup.equals(proxiesBackup1));
  }

  @Test
  void equalsWhenOneHasNullData() {
    ProxiesBackup proxiesBackup = new ProxiesBackup();
    proxiesBackup.setData(createSetOfEntities());

    ProxiesBackup proxiesBackup1 = new ProxiesBackup();
    proxiesBackup1.setData(null);

    assertFalse(proxiesBackup.equals(proxiesBackup1));
  }

  private Set<ProxyEntity> createSetOfEntities() {
    ProxyEntity proxyEntity = new ProxyEntity();
    proxyEntity.setName(UUID.randomUUID().toString());

    ProxyEntity proxyEntity1 = new ProxyEntity();
    proxyEntity1.setName(UUID.randomUUID().toString());

    Set<ProxyEntity> proxyEntities = new HashSet<>();
    proxyEntities.add(proxyEntity);
    proxyEntities.add(proxyEntity1);

    return proxyEntities;
  }
}
