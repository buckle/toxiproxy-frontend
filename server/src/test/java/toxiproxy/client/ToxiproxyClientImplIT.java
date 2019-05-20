package toxiproxy.client;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toxiproxy.client.dto.ClientProxy;

import java.util.Set;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ToxiproxyClientImplIT {

  @Autowired private ToxiproxyClient toxiproxyClient;

  @Test
  void getProxies() {
    Set<ClientProxy> proxies = toxiproxyClient.getProxies();
    proxies.forEach(clientProxy -> System.out.println(clientProxy.getName()));
  }
}
