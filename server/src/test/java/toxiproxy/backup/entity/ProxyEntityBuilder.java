package toxiproxy.backup.entity;

import static toxiproxy.utils.BuilderUtils.getHostPortString;
import static toxiproxy.utils.BuilderUtils.getRandPrependString;

public class ProxyEntityBuilder extends ProxyEntity {

  private ProxyEntityBuilder() {
    this.name(getRandPrependString("Name"));
    this.listen(getHostPortString("listen"));
    this.upstream(getHostPortString("upstream"));
    this.enabled(true);
  }

  public ProxyEntityBuilder name(String name) {
    super.setName(name);
    return this;
  }

  public ProxyEntityBuilder listen(String listen) {
    super.setListen(listen);
    return this;
  }

  public ProxyEntityBuilder upstream(String upstream) {
    super.setUpstream(upstream);
    return this;
  }

  public ProxyEntityBuilder enabled(boolean enabled) {
    super.setEnabled(enabled);
    return this;
  }

  public static ProxyEntityBuilder builder() {
    return new ProxyEntityBuilder();
  }

  public ProxyEntity build() {
    return this;
  }
}
