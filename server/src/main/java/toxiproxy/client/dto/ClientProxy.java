package toxiproxy.client.dto;

import java.util.Objects;
import java.util.Set;

public class ClientProxy {

  private String name;
  private String listen;
  private String upstream;
  private boolean enabled;
  private Set<ClientToxic> toxics;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getListen() {
    return listen;
  }

  public void setListen(String listen) {
    this.listen = listen;
  }

  public String getUpstream() {
    return upstream;
  }

  public void setUpstream(String upstream) {
    this.upstream = upstream;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Set<ClientToxic> getToxics() {
    return toxics;
  }

  public void setToxics(Set<ClientToxic> toxics) {
    this.toxics = toxics;
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;
    ClientProxy proxy = (ClientProxy) o;
    return enabled == proxy.enabled &&
           Objects.equals(name, proxy.name) &&
           Objects.equals(listen, proxy.listen) &&
           Objects.equals(upstream, proxy.upstream) &&
           Objects.equals(toxics, proxy.toxics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, listen, upstream, enabled, toxics);
  }
}
