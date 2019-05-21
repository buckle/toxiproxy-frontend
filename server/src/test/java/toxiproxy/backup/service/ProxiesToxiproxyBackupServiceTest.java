package toxiproxy.backup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import toxiproxy.backup.entity.ProxyEntity;
import toxiproxy.backup.entity.ProxyEntityRepository;
import toxiproxy.backup.mapper.ClientProxyToProxyEntityMapper;
import toxiproxy.backup.mapper.ProxyEntityMapperToClientProxyMapper;
import toxiproxy.client.ToxiproxyClient;
import toxiproxy.client.dto.ClientProxy;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
  @Mock private ProxyEntityMapperToClientProxyMapper proxyEntityMapperToClientProxyMapper;

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
  void restoreBackupToRemote() {
    Set<ProxyEntity> proxyEntities = Sets.newSet();
    ProxiesBackup proxiesBackup = mock(ProxiesBackup.class);
    when(proxiesBackup.getData()).thenReturn(proxyEntities);

    Set<ClientProxy> clientProxies = Sets.newSet();
    clientProxies.add(mock(ClientProxy.class));
    when(proxyEntityMapperToClientProxyMapper.mapProxyEntitiesToClientProxies(proxyEntities)).thenReturn(clientProxies);

    proxiesToxiproxyBackupService.restoreBackupToRemote(proxiesBackup);

    ArgumentCaptor<Set<ClientProxy>> clientProxiesArgumentCaptor = ArgumentCaptor.forClass(Set.class);
    verify(toxiproxyClient, times(1)).populate(clientProxiesArgumentCaptor.capture());
    assertEquals(clientProxies, clientProxiesArgumentCaptor.getValue());
  }

  @Test
  void restoreBackupToRemoteWhenBackupNull() {
    proxiesToxiproxyBackupService.restoreBackupToRemote(null);

    verify(proxyEntityMapperToClientProxyMapper, never()).mapProxyEntitiesToClientProxies(any());
    verify(toxiproxyClient, never()).populate(any());
  }

  @Test
  void restoreBackupToRemoteWhenBackupDataNull() {
    ProxiesBackup proxiesBackup = mock(ProxiesBackup.class);
    when(proxiesBackup.getData()).thenReturn(null);

    proxiesToxiproxyBackupService.restoreBackupToRemote(proxiesBackup);

    verify(proxyEntityMapperToClientProxyMapper, never()).mapProxyEntitiesToClientProxies(any());
    verify(toxiproxyClient, never()).populate(any());
  }

  @Test
  void restoreBackupToRemoteWhenMappedClientProxiesNull() {
    Set<ProxyEntity> proxyEntities = Sets.newSet();
    ProxiesBackup proxiesBackup = mock(ProxiesBackup.class);
    when(proxiesBackup.getData()).thenReturn(proxyEntities);

    when(proxyEntityMapperToClientProxyMapper.mapProxyEntitiesToClientProxies(proxyEntities)).thenReturn(null);

    proxiesToxiproxyBackupService.restoreBackupToRemote(proxiesBackup);

    verify(toxiproxyClient, never()).populate(any());
  }

  @Test
  void restoreBackupToRemoteWhenMappedClientProxiesEmpty() {
    Set<ProxyEntity> proxyEntities = Sets.newSet();
    ProxiesBackup proxiesBackup = mock(ProxiesBackup.class);
    when(proxiesBackup.getData()).thenReturn(proxyEntities);

    Set<ClientProxy> clientProxies = Sets.newSet();
    when(proxyEntityMapperToClientProxyMapper.mapProxyEntitiesToClientProxies(proxyEntities)).thenReturn(clientProxies);

    proxiesToxiproxyBackupService.restoreBackupToRemote(proxiesBackup);

    verify(toxiproxyClient, never()).populate(any());
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

  @Test
  void backupsDifferWhenSame() {
    ProxyEntity proxyEntity = new ProxyEntity();
    proxyEntity.setName(UUID.randomUUID().toString());
    Set<ProxyEntity> proxyEntities = new HashSet<>();
    proxyEntities.add(proxyEntity);

    ProxiesBackup proxiesBackup1 = new ProxiesBackup();
    proxiesBackup1.setData(proxyEntities);

    ProxiesBackup proxiesBackup2 = new ProxiesBackup();
    proxiesBackup2.setData(proxyEntities);

    assertFalse(proxiesToxiproxyBackupService.backupsDiffer(proxiesBackup1, proxiesBackup2));
  }

  @Test
  void backupsDifferWhenDifferent() {
    ProxyEntity proxyEntity1 = new ProxyEntity();
    proxyEntity1.setName(UUID.randomUUID().toString());
    Set<ProxyEntity> proxyEntities1 = new HashSet<>();
    proxyEntities1.add(proxyEntity1);

    ProxiesBackup proxiesBackup1 = new ProxiesBackup();
    proxiesBackup1.setData(proxyEntities1);

    ProxyEntity proxyEntity2 = new ProxyEntity();
    proxyEntity2.setName(UUID.randomUUID().toString());
    Set<ProxyEntity> proxyEntities2 = new HashSet<>();
    proxyEntities2.add(proxyEntity2);

    ProxiesBackup proxiesBackup2 = new ProxiesBackup();
    proxiesBackup2.setData(proxyEntities2);

    assertTrue(proxiesToxiproxyBackupService.backupsDiffer(proxiesBackup1, proxiesBackup2));
  }

  @Test
  void backupsDifferWhenBothNull() {
    assertFalse(proxiesToxiproxyBackupService.backupsDiffer(null, null));
  }

  @Test
  void backupsDifferWhenNewNull() {
    ProxyEntity proxyEntity = new ProxyEntity();
    proxyEntity.setName(UUID.randomUUID().toString());
    Set<ProxyEntity> proxyEntities = new HashSet<>();
    proxyEntities.add(proxyEntity);
    ProxiesBackup proxiesBackup2 = new ProxiesBackup();
    proxiesBackup2.setData(proxyEntities);

    assertTrue(proxiesToxiproxyBackupService.backupsDiffer(null, proxiesBackup2));
  }

  @Test
  void backupsDifferWhenExistingNull() {
    ProxyEntity proxyEntity = new ProxyEntity();
    proxyEntity.setName(UUID.randomUUID().toString());
    Set<ProxyEntity> proxyEntities = new HashSet<>();
    proxyEntities.add(proxyEntity);
    ProxiesBackup proxiesBackup1 = new ProxiesBackup();
    proxiesBackup1.setData(proxyEntities);

    assertTrue(proxiesToxiproxyBackupService.backupsDiffer(proxiesBackup1, null));
  }
}
