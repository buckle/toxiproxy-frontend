package toxiproxy.client;

import toxiproxy.client.dto.ClientProxy;

import java.util.UUID;

public class ClientProxyBuilder extends ClientProxy {

  private ClientProxyBuilder() {
    this.name("Name" + UUID.randomUUID().toString());
    this.listen("listen"+ UUID.randomUUID().toString()+":8474");
    this.upstream("upstream"+ UUID.randomUUID().toString()+":8474");
    this.enabled(true);
  }

  public ClientProxyBuilder name(String name) {
    super.setName(name);
    return this;
  }

  public ClientProxyBuilder listen(String listen) {
    super.setListen(listen);
    return this;
  }

  public ClientProxyBuilder upstream(String upstream) {
    super.setUpstream(upstream);
    return this;
  }

  public ClientProxyBuilder enabled(boolean enabled) {
    super.setEnabled(enabled);
    return this;
  }

  public static ClientProxyBuilder builder() {
    return new ClientProxyBuilder();
  }

  public ClientProxy build() {
    return this;
  }
}
