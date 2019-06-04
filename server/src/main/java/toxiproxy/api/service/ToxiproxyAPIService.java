package toxiproxy.api.service;

import toxiproxy.client.dto.ClientProxy;
import toxiproxy.client.dto.ClientToxic;

import java.util.Set;

public interface ToxiproxyAPIService {

  Set<ClientProxy> getProxies();

  ClientProxy getProxy(String proxyName);

  ClientProxy createProxy(ClientProxy clientProxy);

  ClientProxy updateProxy(ClientProxy clientProxy);

  void deleteProxy(String proxyName);

  ClientToxic addToxic(String proxyName, ClientToxic clientToxic);

  ClientToxic updateToxic(String proxyName, ClientToxic clientToxic);

  void deleteToxic(String proxyName, String toxicName);

  String getServiceVersion();

}
