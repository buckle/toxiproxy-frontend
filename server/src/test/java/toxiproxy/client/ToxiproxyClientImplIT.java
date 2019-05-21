package toxiproxy.client;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toxiproxy.client.dto.ClientProxy;
import toxiproxy.client.dto.ClientProxyBuilder;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ToxiproxyClientImplIT {

  @Autowired private ToxiproxyClient toxiproxyClient;

  @Test
  void populate() {
    Set<ClientProxy> clientProxies = new HashSet<>();
    clientProxies.add(ClientProxyBuilder.builder().listen("localhost:5556").build());

    Set<ClientProxy> populatedProxies = toxiproxyClient.populate(clientProxies);

    assertNotNull(populatedProxies);
  }

  @Test
  void getProxies() {
    Set<ClientProxy> proxies = toxiproxyClient.getProxies();
    proxies.forEach(clientProxy -> System.out.println(clientProxy.getName()));
  }
}
