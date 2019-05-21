package toxiproxy.backup.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import toxiproxy.backup.entity.ProxyEntity;
import toxiproxy.backup.entity.ProxyEntityBuilder;
import toxiproxy.client.dto.ClientProxy;

import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
public class ProxyEntityMapperToClientProxyMapperTest {

  @InjectMocks private ProxyEntityMapperToClientProxyMapper proxyEntityMapperToClientProxyMapper = spy(ProxyEntityMapperToClientProxyMapper.class);

  @Test
  void mapProxyEntitiesToClientProxies() {
    ClientProxy clientProxy1 = mock(ClientProxy.class);
    ProxyEntity proxyEntity1 = mock(ProxyEntity.class);
    doReturn(clientProxy1).when(proxyEntityMapperToClientProxyMapper).mapProxyEntityToClientProxy(proxyEntity1);

    ClientProxy clientProxy2 = mock(ClientProxy.class);
    ProxyEntity proxyEntity2 = mock(ProxyEntity.class);
    doReturn(clientProxy2).when(proxyEntityMapperToClientProxyMapper).mapProxyEntityToClientProxy(proxyEntity2);

    Set<ProxyEntity> proxyEntities = Sets.newSet(proxyEntity1, proxyEntity2);

    Set<ClientProxy> clientProxies = proxyEntityMapperToClientProxyMapper.mapProxyEntitiesToClientProxies(proxyEntities);

    assertNotNull(clientProxies);
    assertEquals(proxyEntities.size(), clientProxies.size());
    assertTrue(clientProxies.contains(clientProxy1));
    assertTrue(clientProxies.contains(clientProxy2));
  }

  @Test
  void mapProxyEntitiesToClientProxiesWhenNullProxyEntities() {
    Set<ClientProxy> clientProxies = proxyEntityMapperToClientProxyMapper.mapProxyEntitiesToClientProxies(null);

    assertNull(clientProxies);
  }

  @Test
  void mapProxyEntitiesToClientProxiesWhenOneProxyEntityNull() {
    ClientProxy clientProxy1 = mock(ClientProxy.class);
    ProxyEntity proxyEntity1 = mock(ProxyEntity.class);
    doReturn(clientProxy1).when(proxyEntityMapperToClientProxyMapper).mapProxyEntityToClientProxy(proxyEntity1);

    ProxyEntity proxyEntity2 = mock(ProxyEntity.class);
    doReturn(null).when(proxyEntityMapperToClientProxyMapper).mapProxyEntityToClientProxy(proxyEntity2);

    Set<ProxyEntity> proxyEntities = Sets.newSet(proxyEntity1, proxyEntity2);

    Set<ClientProxy> clientProxies = proxyEntityMapperToClientProxyMapper.mapProxyEntitiesToClientProxies(proxyEntities);

    assertNotNull(clientProxies);
    assertEquals(1, clientProxies.size());
    assertTrue(clientProxies.contains(clientProxy1));
  }

  @Test
  void mapProxyEntitiesToClientProxiesWhenAllProxiesMapNull() {
    ProxyEntity proxyEntity1 = mock(ProxyEntity.class);
    doReturn(null).when(proxyEntityMapperToClientProxyMapper).mapProxyEntityToClientProxy(proxyEntity1);

    ProxyEntity proxyEntity2 = mock(ProxyEntity.class);
    doReturn(null).when(proxyEntityMapperToClientProxyMapper).mapProxyEntityToClientProxy(proxyEntity2);

    Set<ProxyEntity> proxyEntities = Sets.newSet(proxyEntity1, proxyEntity2);

    Set<ClientProxy> clientProxies = proxyEntityMapperToClientProxyMapper.mapProxyEntitiesToClientProxies(proxyEntities);

    assertNull(clientProxies);
  }

  @Test
  void mapProxyEntityToClientProxy() {
    ProxyEntity proxyEntity = ProxyEntityBuilder.builder().build();

    ClientProxy clientProxy = proxyEntityMapperToClientProxyMapper.mapProxyEntityToClientProxy(proxyEntity);

    assertNotNull(clientProxy);

    assertFalse(isEmpty(proxyEntity.getName()));
    assertEquals(proxyEntity.getName(), clientProxy.getName());

    assertFalse(isEmpty(proxyEntity.getListen()));
    assertEquals(proxyEntity.getListen(), clientProxy.getListen());

    assertFalse(isEmpty(proxyEntity.getUpstream()));
    assertEquals(proxyEntity.getUpstream(), clientProxy.getUpstream());

    assertTrue(proxyEntity.isEnabled());
    assertEquals(proxyEntity.isEnabled(), clientProxy.isEnabled());
  }

  @Test
  void mapProxyEntityToClientProxyWhenNullProxyEntity() {
    ClientProxy clientProxy = proxyEntityMapperToClientProxyMapper.mapProxyEntityToClientProxy(null);

    assertNull(clientProxy);
  }
}
