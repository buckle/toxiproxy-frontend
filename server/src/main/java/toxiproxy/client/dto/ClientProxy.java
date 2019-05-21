package toxiproxy.client.dto;

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
}
