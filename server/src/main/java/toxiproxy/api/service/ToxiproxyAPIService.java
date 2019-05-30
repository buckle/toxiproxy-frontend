package toxiproxy.api.service;

import toxiproxy.client.dto.ClientProxy;

import java.util.Set;

public interface ToxiproxyAPIService {

  Set<ClientProxy> getProxies();

  ClientProxy getProxy(String proxyName);

  ClientProxy createProxy(ClientProxy clientProxy);

  String getServiceVersion();

}
