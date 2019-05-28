package toxiproxy.client.dto;

import java.util.Objects;

public class ClientProxy {

  private String name;
  private String listen;
  private String upstream;
  private boolean enabled;

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

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;
    ClientProxy that = (ClientProxy) o;
    return enabled == that.enabled &&
           Objects.equals(name, that.name) &&
           Objects.equals(listen, that.listen) &&
           Objects.equals(upstream, that.upstream);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, listen, upstream, enabled);
  }
}
