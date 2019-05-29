package toxiproxy.client.dto;

import java.util.HashSet;
import java.util.Set;

import static toxiproxy.utils.BuilderUtils.getHostPortString;
import static toxiproxy.utils.BuilderUtils.getRandPrependString;

public class ClientProxyBuilder {

  private ClientProxy clientProxy;

  private ClientProxyBuilder() {
    this.clientProxy = new ClientProxy();
    this.name(getRandPrependString("Name"));
    this.listen(getHostPortString("listen"));
    this.upstream(getHostPortString("upstream"));
    this.enabled(true);
    this.toxics(new HashSet<>());
  }

  public ClientProxyBuilder name(String name) {
    clientProxy.setName(name);
    return this;
  }

  public ClientProxyBuilder listen(String listen) {
    clientProxy.setListen(listen);
    return this;
  }

  public ClientProxyBuilder upstream(String upstream) {
    clientProxy.setUpstream(upstream);
    return this;
  }

  public ClientProxyBuilder enabled(boolean enabled) {
    clientProxy.setEnabled(enabled);
    return this;
  }

  public ClientProxyBuilder toxics(Set<ClientToxic> clientToxics) {
    clientProxy.setToxics(clientToxics);
    return this;
  }

  public ClientProxyBuilder toxic(ClientToxic clientToxic) {
    Set<ClientToxic> toxics = clientProxy.getToxics();
    if(toxics == null) toxics = new HashSet<>();
    toxics.add(clientToxic);
    clientProxy.setToxics(toxics);
    return this;
  }

  public static ClientProxyBuilder builder() {
    return new ClientProxyBuilder();
  }

  public ClientProxy build() {
    return clientProxy;
  }
}
