package toxiproxy.client.dto;

import java.util.HashMap;
import java.util.Map;

import static toxiproxy.utils.BuilderUtils.getRandPrependString;

public class ClientToxicBuilder {

  private ClientToxic clientToxic;

  private ClientToxicBuilder() {
    this.clientToxic = new ClientToxic();
    this.name(getRandPrependString("ToxicName"));
    this.type("latency");
    this.stream("downstream");
    this.toxicity(1.0);
    this.attribute("latency", 300L);
    this.attribute("jitter", 100L);
  }

  public ClientToxicBuilder name(String name) {
    clientToxic.setName(name);
    return this;
  }

  public ClientToxicBuilder type(String type) {
    clientToxic.setType(type);
    return this;
  }

  public ClientToxicBuilder stream(String stream) {
    clientToxic.setStream(stream);
    return this;
  }

  public ClientToxicBuilder toxicity(Double toxicity) {
    clientToxic.setToxicity(toxicity);
    return this;
  }

  public ClientToxicBuilder attributes(Map<String, Long> attributes) {
    clientToxic.setAttributes(attributes);
    return this;
  }

  public ClientToxicBuilder attribute(String name, Long value) {
    Map<String, Long> attributes = clientToxic.getAttributes();
    if(attributes == null) attributes = new HashMap<>();
    attributes.put(name, value);
    clientToxic.setAttributes(attributes);
    return this;
  }

  public static ClientToxicBuilder builder() {
    return new ClientToxicBuilder();
  }

  public ClientToxic build() {
    return clientToxic;
  }
}
