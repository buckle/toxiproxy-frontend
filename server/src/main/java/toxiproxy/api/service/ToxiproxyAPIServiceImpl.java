package toxiproxy.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import toxiproxy.client.ToxiproxyClient;
import toxiproxy.client.dto.ClientProxy;

import java.util.Set;

/**
 * ToxiproxyAPIServiceImpl
 * <p>
 * This implementation should probably be using a different domain than the client domain, but just trying to get MVP functionality out at this time.
 * We can create a different domain to map to the client domain later on when we want to extend functionality.
 * <p>
 * Currently this is just a thin wrapper around the ToxiproxyClient.
 */
@Service
public class ToxiproxyAPIServiceImpl implements ToxiproxyAPIService {

  @Autowired private ToxiproxyClient toxiproxyClient;

  @Override
  public Set<ClientProxy> getProxies() {
    return toxiproxyClient.getProxies();
  }

  @Override
  public String getServiceVersion() {
    return toxiproxyClient.getVersion();
  }
}
