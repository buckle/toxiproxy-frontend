package toxiproxy.backup.mapper;

import org.springframework.stereotype.Service;
import toxiproxy.backup.entity.ProxyEntity;
import toxiproxy.client.dto.ClientProxy;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientProxyToProxyEntityMapper {

  public Set<ProxyEntity> mapClientProxiesToProxyEntities(Set<ClientProxy> clientProxies) {
    if(clientProxies != null) {
      Set<ProxyEntity> proxyEntities = clientProxies.stream()
                                                    .map(clientProxy -> mapClientProxyToProxyEntity(clientProxy))
                                                    .filter(proxyEntity -> proxyEntity != null)
                                                    .collect(Collectors.toSet());

      return !proxyEntities.isEmpty() ? proxyEntities : null;
    }

    return null;
  }

  public ProxyEntity mapClientProxyToProxyEntity(ClientProxy clientProxy) {
    if(clientProxy != null) {
      ProxyEntity proxyEntity = new ProxyEntity();
      proxyEntity.setName(clientProxy.getName());
      proxyEntity.setListen(clientProxy.getListen());
      proxyEntity.setUpstream(clientProxy.getUpstream());
      proxyEntity.setEnabled(clientProxy.isEnabled());
      return proxyEntity;
    }

    return null;
  }
}
