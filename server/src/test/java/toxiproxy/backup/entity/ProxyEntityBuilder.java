package toxiproxy.backup.entity;

import static toxiproxy.utils.BuilderUtils.getHostPortString;
import static toxiproxy.utils.BuilderUtils.getRandPrependString;

public class ProxyEntityBuilder {

  private ProxyEntity proxyEntity;

  private ProxyEntityBuilder() {
    this.proxyEntity = new ProxyEntity();
    this.name(getRandPrependString("Name"));
    this.listen(getHostPortString("listen"));
    this.upstream(getHostPortString("upstream"));
    this.enabled(true);
  }

  public ProxyEntityBuilder name(String name) {
    proxyEntity.setName(name);
    return this;
  }

  public ProxyEntityBuilder listen(String listen) {
    proxyEntity.setListen(listen);
    return this;
  }

  public ProxyEntityBuilder upstream(String upstream) {
    proxyEntity.setUpstream(upstream);
    return this;
  }

  public ProxyEntityBuilder enabled(boolean enabled) {
    proxyEntity.setEnabled(enabled);
    return this;
  }

  public static ProxyEntityBuilder builder() {
    return new ProxyEntityBuilder();
  }

  public ProxyEntity build() {
    return proxyEntity;
  }
}
