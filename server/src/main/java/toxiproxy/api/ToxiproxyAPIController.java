package toxiproxy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toxiproxy.api.service.ToxiproxyAPIService;
import toxiproxy.client.dto.ClientProxy;

import java.util.Set;

@RestController
@RequestMapping(ToxiproxyAPIController.API_ENDPOINT)
public class ToxiproxyAPIController {

  public static final String API_ENDPOINT = "api";
  public static final String PROXIES_ENDPOINT = "proxies";
  public static final String SERVICE_VERSION_ENDPOINT = "service-version";

  @Autowired private ToxiproxyAPIService toxiproxyAPIService;

  @GetMapping(ToxiproxyAPIController.PROXIES_ENDPOINT)
  public Set<ClientProxy> getProxies() {
    return toxiproxyAPIService.getProxies();
  }

  @GetMapping(ToxiproxyAPIController.SERVICE_VERSION_ENDPOINT)
  public String getServiceVersion() {
    return toxiproxyAPIService.getServiceVersion();
  }

}
