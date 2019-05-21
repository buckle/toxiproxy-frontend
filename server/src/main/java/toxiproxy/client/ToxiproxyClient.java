package toxiproxy.client;

import toxiproxy.client.dto.ClientProxy;

import java.util.Set;

public interface ToxiproxyClient {

  Set<ClientProxy> populate(Set<ClientProxy> clientProxies);

  Set<ClientProxy> getProxies();

}
