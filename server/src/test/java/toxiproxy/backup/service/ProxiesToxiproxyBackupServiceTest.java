package toxiproxy.backup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import toxiproxy.backup.entity.ProxyEntity;
import toxiproxy.backup.entity.ProxyEntityRepository;
import toxiproxy.backup.mapper.ClientProxyToProxyEntityMapper;
import toxiproxy.client.ToxiproxyClient;
import toxiproxy.client.dto.ClientProxy;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProxiesToxiproxyBackupServiceTest {

  @InjectMocks private ProxiesToxiproxyBackupService proxiesToxiproxyBackupService;
  @Mock private ProxyEntityRepository proxyEntityRepository;
  @Mock private ToxiproxyClient toxiproxyClient;
  @Mock private ClientProxyToProxyEntityMapper clientProxyToProxyEntityMapper;

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
    clientProxies.add(mock(ClientProxy.class));
    when(toxiproxyClient.getProxies()).thenReturn(clientProxies);
    Set<ProxyEntity> proxyEntities = new HashSet<>();
    proxyEntities.add(mock(ProxyEntity.class));
    when(clientProxyToProxyEntityMapper.mapClientProxiesToProxyEntities(clientProxies)).thenReturn(proxyEntities);

    ProxiesBackup backupFromRemote = (ProxiesBackup) proxiesToxiproxyBackupService.getBackupFromRemote();

    assertNotNull(backupFromRemote);
    assertEquals(proxyEntities, backupFromRemote.getData());
  }

  @Test
  void getBackupFromRemoteWhenClientProxiesNull() {
    when(toxiproxyClient.getProxies()).thenReturn(null);

    ProxiesBackup backupFromRemote = (ProxiesBackup) proxiesToxiproxyBackupService.getBackupFromRemote();

    assertNull(backupFromRemote);
    verify(clientProxyToProxyEntityMapper, never()).mapClientProxiesToProxyEntities(any());
  }

  @Test
  void getBackupFromRemoteWhenClientProxiesEmpty() {
    when(toxiproxyClient.getProxies()).thenReturn(Sets.newSet());

    ProxiesBackup backupFromRemote = (ProxiesBackup) proxiesToxiproxyBackupService.getBackupFromRemote();

    assertNull(backupFromRemote);
    verify(clientProxyToProxyEntityMapper, never()).mapClientProxiesToProxyEntities(any());
  }

  @Test
  void getBackupFromRemoteWhenMappedEntitiesNull() {
    Set<ClientProxy> clientProxies = new HashSet<>();
    clientProxies.add(mock(ClientProxy.class));
    when(toxiproxyClient.getProxies()).thenReturn(clientProxies);
    when(clientProxyToProxyEntityMapper.mapClientProxiesToProxyEntities(clientProxies)).thenReturn(null);

    ProxiesBackup backupFromRemote = (ProxiesBackup) proxiesToxiproxyBackupService.getBackupFromRemote();

    assertNull(backupFromRemote);
    verify(toxiproxyClient, times(1)).getProxies();
  }

  @Test
  void getBackupFromRemoteWhenMappedEntitiesEmpty() {
    Set<ClientProxy> clientProxies = new HashSet<>();
    clientProxies.add(mock(ClientProxy.class));
    when(toxiproxyClient.getProxies()).thenReturn(clientProxies);
    when(clientProxyToProxyEntityMapper.mapClientProxiesToProxyEntities(clientProxies)).thenReturn(Sets.newSet());

    ProxiesBackup backupFromRemote = (ProxiesBackup) proxiesToxiproxyBackupService.getBackupFromRemote();

    assertNull(backupFromRemote);
    verify(toxiproxyClient, times(1)).getProxies();
  }

  @Test
  void setBackup() {
    Set<ProxyEntity> proxyEntities = new HashSet<>();
    proxyEntities.add(mock(ProxyEntity.class));
    ProxiesBackup proxiesBackup = mock(ProxiesBackup.class);
    when(proxiesBackup.getData()).thenReturn(proxyEntities);

    proxiesToxiproxyBackupService.setBackup(proxiesBackup);

    InOrder inOrder = inOrder(proxyEntityRepository);
    inOrder.verify(proxyEntityRepository, times(1)).deleteAll();
    inOrder.verify(proxyEntityRepository, times(1)).saveAll(proxyEntities);
  }

  @Test
  void setBackupWhenNull() {
    proxiesToxiproxyBackupService.setBackup(null);

    verify(proxyEntityRepository, never()).deleteAll();
    verify(proxyEntityRepository, never()).saveAll(any());
  }

  @Test
  void setBackupWhenDataNull() {
    ProxiesBackup proxiesBackup = mock(ProxiesBackup.class);
    when(proxiesBackup.getData()).thenReturn(null);

    proxiesToxiproxyBackupService.setBackup(proxiesBackup);

    verify(proxyEntityRepository, never()).deleteAll();
    verify(proxyEntityRepository, never()).saveAll(any());
  }

  @Test
  void setBackupWhenDataEmpty() {
    Set<ProxyEntity> proxyEntities = new HashSet<>();
    ProxiesBackup proxiesBackup = mock(ProxiesBackup.class);
    when(proxiesBackup.getData()).thenReturn(proxyEntities);

    proxiesToxiproxyBackupService.setBackup(proxiesBackup);

    verify(proxyEntityRepository, never()).deleteAll();
    verify(proxyEntityRepository, never()).saveAll(any());
  }
}
