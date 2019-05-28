package toxiproxy.client.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientCloningUtils {

  public static ClientProxy createProxyClone(ClientProxy clientProxy) {
    ClientProxy clientProxyClone = new ClientProxy();
    clientProxyClone.setName(clientProxy.getName());
    clientProxyClone.setListen(clientProxy.getListen());
    clientProxyClone.setUpstream(clientProxy.getUpstream());
    clientProxyClone.setEnabled(clientProxy.isEnabled());

    if(clientProxy.getToxics() != null) {
      clientProxyClone.setToxics(clientProxy.getToxics()
                                            .stream()
                                            .map(ClientCloningUtils::createToxicClone)
                                            .collect(Collectors.toSet()));

    }

    return clientProxyClone;
  }

  public static ClientToxic createToxicClone(ClientToxic clientToxic) {
    ClientToxic clientToxicClone = new ClientToxic();
    clientToxicClone.setName(clientToxic.getName());
    clientToxicClone.setType(clientToxic.getType());
    clientToxicClone.setStream(clientToxic.getStream());

    if(clientToxic.getToxicity() != null) {
      clientToxicClone.setToxicity(Double.valueOf(clientToxic.getToxicity()));
    }

    if(clientToxic.getAttributes() != null) {
      Map<String, Long> attributeClone = new HashMap<>();
      attributeClone.putAll(clientToxic.getAttributes());
      clientToxicClone.setAttributes(attributeClone);
    }

    return clientToxicClone;
  }
}
