package toxiproxy.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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

  protected String getURL() {
    return this.url;
  }
}
