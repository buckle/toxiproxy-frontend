package toxiproxy.backup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toxiproxy.backup.entity.ProxyEntity;
import toxiproxy.backup.entity.ProxyEntityRepository;
import toxiproxy.client.ToxiproxyClient;
import toxiproxy.client.dto.ClientProxy;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProxiesToxiproxyBackupServiceTest {

  @InjectMocks private ProxiesToxiproxyBackupService proxiesToxiproxyBackupService;
  @Mock private ProxyEntityRepository proxyEntityRepository;
  @Mock private ToxiproxyClient toxiproxyClient;

  @Test
  void getCurrentBackup() {
    Set<ProxyEntity> proxyEntities = new HashSet<>();
    proxyEntities.add(new ProxyEntity());
    when(proxyEntityRepository.findAll()).thenReturn(proxyEntities);

    ProxiesBackup currentBackup = (ProxiesBackup) proxiesToxiproxyBackupService.getCurrentBackup();

    assertNotNull(currentBackup);
    assertEquals(proxyEntities, currentBackup.getData());
  }

  @Test
  void getCurrentBackupWhenNullEntitiesInDB() {
    when(proxyEntityRepository.findAll()).thenReturn(null);

    ProxiesBackup currentBackup = (ProxiesBackup) proxiesToxiproxyBackupService.getCurrentBackup();

    assertNull(currentBackup);
  }

  @Test
  void getCurrentBackupWhenEntitiesEmpty() {
    when(proxyEntityRepository.findAll()).thenReturn(new HashSet<>());

    ProxiesBackup currentBackup = (ProxiesBackup) proxiesToxiproxyBackupService.getCurrentBackup();

    assertNull(currentBackup);
  }

  @Test
  void getBackupFromRemote() {
    Set<ClientProxy> clientProxies = new HashSet<>();
    when(toxiproxyClient.getProxies()).thenReturn(clientProxies);

  }
}
