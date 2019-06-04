package toxiproxy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toxiproxy.api.service.ToxiproxyAPIService;
import toxiproxy.client.dto.ClientProxy;
import toxiproxy.client.dto.ClientToxic;

import java.util.Set;

@RestController
@RequestMapping(ToxiproxyAPIController.API_ENDPOINT)
public class ToxiproxyAPIController {

  public static final String API_ENDPOINT = "api";
  public static final String PROXIES_ENDPOINT = "proxies";
  public static final String TOXICS_ENDPOINT = "toxics";
  public static final String SERVICE_VERSION_ENDPOINT = "service-version";

  @Autowired private ToxiproxyAPIService toxiproxyAPIService;

  @GetMapping(ToxiproxyAPIController.PROXIES_ENDPOINT)
  public Set<ClientProxy> getProxies() {
    return toxiproxyAPIService.getProxies();
  }

  @GetMapping(ToxiproxyAPIController.PROXIES_ENDPOINT + "/{proxyName}")
  public ClientProxy getProxy(@PathVariable String proxyName) {
    return toxiproxyAPIService.getProxy(proxyName);
  }

  @PostMapping(ToxiproxyAPIController.PROXIES_ENDPOINT)
  public ClientProxy createProxy(@RequestBody ClientProxy clientProxy) {
    return toxiproxyAPIService.createProxy(clientProxy);
  }

  @PostMapping(ToxiproxyAPIController.PROXIES_ENDPOINT + "/{proxyName}")
  public ClientProxy updateProxy(@PathVariable String proxyName, @RequestBody ClientProxy clientProxy) {
    return toxiproxyAPIService.updateProxy(clientProxy);
  }

  @DeleteMapping(ToxiproxyAPIController.PROXIES_ENDPOINT + "/{proxyName}")
  public void deleteProxy(@PathVariable String proxyName) {
    toxiproxyAPIService.deleteProxy(proxyName);
  }

  @PostMapping(ToxiproxyAPIController.PROXIES_ENDPOINT + "/{proxyName}/" + ToxiproxyAPIController.TOXICS_ENDPOINT)
  public ClientToxic addToxic(@PathVariable String proxyName, @RequestBody ClientToxic clientToxic) {
    return toxiproxyAPIService.addToxic(proxyName, clientToxic);
  }

  @GetMapping(ToxiproxyAPIController.SERVICE_VERSION_ENDPOINT)
  public String getServiceVersion() {
    return toxiproxyAPIService.getServiceVersion();
  }

}
