package toxiproxy.backup.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import toxiproxy.backup.entity.ProxyEntity;
import toxiproxy.client.ClientProxyBuilder;
import toxiproxy.client.dto.ClientProxy;

import java.util.HashSet;
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
public class ClientProxyToProxyEntityMapperTest {

  @InjectMocks private ClientProxyToProxyEntityMapper clientProxyToProxyEntityMapper = spy(ClientProxyToProxyEntityMapper.class);

  @Test
  void mapClientProxiesToProxyEntities() {
    ProxyEntity proxyEntity1 = mock(ProxyEntity.class);
    ClientProxy clientProxy1 = ClientProxyBuilder.builder().build();
    doReturn(proxyEntity1).when(clientProxyToProxyEntityMapper).mapClientProxyToProxyEntity(clientProxy1);

    ProxyEntity proxyEntity2 = mock(ProxyEntity.class);
    ClientProxy clientProxy2 = ClientProxyBuilder.builder().build();
    doReturn(proxyEntity2).when(clientProxyToProxyEntityMapper).mapClientProxyToProxyEntity(clientProxy2);

    Set<ClientProxy> clientProxies = new HashSet<>();
    clientProxies.add(clientProxy1);
    clientProxies.add(clientProxy2);

    Set<ProxyEntity> proxyEntities = clientProxyToProxyEntityMapper.mapClientProxiesToProxyEntities(clientProxies);

    assertNotNull(proxyEntities);
    assertEquals(clientProxies.size(), proxyEntities.size());
    assertTrue(proxyEntities.contains(proxyEntity1));
    assertTrue(proxyEntities.contains(proxyEntity2));
  }

  @Test
  void mapClientProxiesToProxyEntitiesWhenClientProxiesNull() {
    Set<ProxyEntity> proxyEntities = clientProxyToProxyEntityMapper.mapClientProxiesToProxyEntities(null);

    assertNull(proxyEntities);
  }

  @Test
  void mapClientProxiesToProxyEntitiesWhenOneMappedAsNull() {
    ProxyEntity proxyEntity1 = mock(ProxyEntity.class);
    ClientProxy clientProxy1 = ClientProxyBuilder.builder().build();
    doReturn(proxyEntity1).when(clientProxyToProxyEntityMapper).mapClientProxyToProxyEntity(clientProxy1);

    ClientProxy clientProxy2 = ClientProxyBuilder.builder().build();
    doReturn(null).when(clientProxyToProxyEntityMapper).mapClientProxyToProxyEntity(clientProxy2);

    Set<ClientProxy> clientProxies = new HashSet<>();
    clientProxies.add(clientProxy1);
    clientProxies.add(clientProxy2);

    Set<ProxyEntity> proxyEntities = clientProxyToProxyEntityMapper.mapClientProxiesToProxyEntities(clientProxies);

    assertNotNull(proxyEntities);
    assertEquals(1, proxyEntities.size());
    assertTrue(proxyEntities.contains(proxyEntity1));
  }

  @Test
  void mapClientProxiesToProxyEntitiesWhenAllMapAsNull() {
    ClientProxy clientProxy1 = ClientProxyBuilder.builder().build();
    doReturn(null).when(clientProxyToProxyEntityMapper).mapClientProxyToProxyEntity(clientProxy1);

    ClientProxy clientProxy2 = ClientProxyBuilder.builder().build();
    doReturn(null).when(clientProxyToProxyEntityMapper).mapClientProxyToProxyEntity(clientProxy2);

    Set<ClientProxy> clientProxies = new HashSet<>();
    clientProxies.add(clientProxy1);
    clientProxies.add(clientProxy2);

    Set<ProxyEntity> proxyEntities = clientProxyToProxyEntityMapper.mapClientProxiesToProxyEntities(clientProxies);

    assertNull(proxyEntities);
  }

  @Test
  void mapClientProxyToProxyEntity() {
    ClientProxy clientProxy = ClientProxyBuilder.builder().build();

    ProxyEntity proxyEntity = clientProxyToProxyEntityMapper.mapClientProxyToProxyEntity(clientProxy);

    assertNotNull(proxyEntity);

    assertFalse(isEmpty(clientProxy.getName()));
    assertEquals(clientProxy.getName(), proxyEntity.getName());

    assertFalse(isEmpty(clientProxy.getListen()));
    assertEquals(clientProxy.getListen(), proxyEntity.getListen());

    assertFalse(isEmpty(clientProxy.getUpstream()));
    assertEquals(clientProxy.getUpstream(), proxyEntity.getUpstream());

    assertTrue(clientProxy.isEnabled());
    assertEquals(clientProxy.isEnabled(), proxyEntity.isEnabled());
  }

  @Test
  void mapClientProxyToProxyEntityWhenNullClientProxy() {
    ProxyEntity proxyEntity = clientProxyToProxyEntityMapper.mapClientProxyToProxyEntity(null);

    assertNull(proxyEntity);
  }
}
