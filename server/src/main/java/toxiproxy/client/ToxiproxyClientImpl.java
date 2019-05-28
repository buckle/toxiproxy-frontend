package toxiproxy.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import toxiproxy.client.dto.ClientPopulateResponse;
import toxiproxy.client.dto.ClientProxy;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
public class ToxiproxyClientImpl implements ToxiproxyClient {

  @NotNull
  @Value("${toxiproxy.url}")
  private String url;

  @Autowired private RestTemplate toxiproxyClientRestTemplate;

  @Override
  public Set<ClientProxy> populate(Set<ClientProxy> clientProxies) {
    if(clientProxies != null && !clientProxies.isEmpty()) {
      ClientPopulateResponse clientPopulateResponse = toxiproxyClientRestTemplate.postForObject(getURL() + "/populate",
                                                                                                clientProxies,
                                                                                                ClientPopulateResponse.class);

      return clientPopulateResponse.getProxies();
    }

    return null;
  }

  @Override
  public Set<ClientProxy> getProxies() {
    ParameterizedTypeReference<HashMap<String, ClientProxy>> parameterizedTypeReference = new ParameterizedTypeReference<>() {};

    ResponseEntity<HashMap<String, ClientProxy>> exchange =
        toxiproxyClientRestTemplate.exchange(RequestEntity.get(URI.create(getURL() + "/proxies"))
                                                          .build(),
                                             parameterizedTypeReference);

    HashMap<String, ClientProxy> body = exchange.getBody();

    if(body != null && !body.isEmpty()) {
      return new HashSet<>(exchange.getBody().values());
    }

    return null;
  }

  @Override
  public ClientProxy getProxy(String proxyName) {
    if(proxyName != null) {
      try {
        return toxiproxyClientRestTemplate.getForObject(getURL() + "/proxies/{proxy-name}", ClientProxy.class, proxyName);
      } catch(HttpClientErrorException hcee) {
        if(hcee instanceof HttpClientErrorException.NotFound) {
          return null;
        }
      }
    }

    return null;
  }

  @Override
  public ClientProxy createProxy(ClientProxy newProxy) {
    if(newProxy != null) {
      return toxiproxyClientRestTemplate.postForObject(getURL() + "/proxies", newProxy, ClientProxy.class);
    }

    return null;
  }

  @Override
  public void deleteProxy(String proxyName) {
    if(proxyName != null) {
      try {
        toxiproxyClientRestTemplate.delete(getURL() + "/proxies/{proxy-name}", proxyName);
      } catch(HttpClientErrorException hcee) {
        if(!(hcee instanceof HttpClientErrorException.NotFound)) {
          throw hcee;
        }
      }
    }
  }

  @Override
  public void deleteAllProxies() {
    Set<ClientProxy> proxies = getProxies();
    if(proxies != null) {
      proxies.forEach(clientProxy -> deleteProxy(clientProxy.getName()));
    }
  }

  protected String getURL() {
    return this.url;
  }
}
