package toxiproxy.backup.mapper;

import org.springframework.stereotype.Service;
import toxiproxy.backup.entity.ProxyEntity;
import toxiproxy.client.dto.ClientProxy;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProxyEntityMapperToClientProxyMapper {

  public Set<ClientProxy> mapProxyEntitiesToClientProxies(Set<ProxyEntity> proxyEntities) {
    if(proxyEntities != null) {
      Set<ClientProxy> clientProxies = proxyEntities.stream()
                                                    .map(proxyEntity -> mapProxyEntityToClientProxy(proxyEntity))
                                                    .filter(clientProxy -> clientProxy != null)
                                                    .collect(Collectors.toSet());

      return !clientProxies.isEmpty() ? clientProxies : null;
    }

    return null;
  }

  public ClientProxy mapProxyEntityToClientProxy(ProxyEntity proxyEntity) {
    if(proxyEntity != null) {
      ClientProxy clientProxy = new ClientProxy();
      clientProxy.setName(proxyEntity.getName());
      clientProxy.setListen(proxyEntity.getListen());
      clientProxy.setUpstream(proxyEntity.getUpstream());
      clientProxy.setEnabled(proxyEntity.isEnabled());
      return clientProxy;
    }

    return null;
  }
}
