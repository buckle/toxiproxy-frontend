package toxiproxy.client.dto;

import static toxiproxy.utils.BuilderUtils.getHostPortString;
import static toxiproxy.utils.BuilderUtils.getRandPrependString;

public class ClientProxyBuilder extends ClientProxy {

  private ClientProxyBuilder() {
    this.name(getRandPrependString("Name"));
    this.listen(getHostPortString("listen"));
    this.upstream(getHostPortString("upstream"));
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
