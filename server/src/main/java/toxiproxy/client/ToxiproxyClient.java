package toxiproxy.client;

import toxiproxy.client.dto.ClientProxy;

import java.util.Set;

public interface ToxiproxyClient {

  Set<ClientProxy> populate(Set<ClientProxy> clientProxies);

  Set<ClientProxy> getProxies();

  ClientProxy getProxy(String proxyName);

  ClientProxy createProxy(ClientProxy clientProxy);

  void deleteProxy(String proxyName);

  void deleteAllProxies();

}
