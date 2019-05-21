package toxiproxy.client.dto;

import java.util.Set;

public class ClientPopulateResponse {

  private Set<ClientProxy> proxies;

  public Set<ClientProxy> getProxies() {
    return proxies;
  }

  public void setProxies(Set<ClientProxy> proxies) {
    this.proxies = proxies;
  }
}
