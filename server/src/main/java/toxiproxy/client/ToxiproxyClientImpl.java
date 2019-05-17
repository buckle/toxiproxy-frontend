package toxiproxy.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;

@Service
public class ToxiproxyClientImpl implements ToxiproxyClient {

  @NotNull
  @Value("${toxiproxy.host}")
  private String host;

  @Autowired private RestTemplate toxiproxyClientRestTemplate;

  @Override
  public String getProxiesRawString() {
    String rawProxies = toxiproxyClientRestTemplate.getForObject(getHost() + "/proxies", String.class);
    rawProxies = rawProxies.replaceAll(" ", "");
    return rawProxies.equals("{}") ? null : rawProxies;
  }

  protected String getHost() {
    return this.host;
  }
}
