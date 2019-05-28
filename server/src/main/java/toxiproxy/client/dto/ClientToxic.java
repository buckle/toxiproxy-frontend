package toxiproxy.client.dto;

import java.util.Map;
import java.util.Objects;

public class ClientToxic {

  private String name;
  private String type;
  private String stream;
  private Double toxicity;
  private Map<String, Long> attributes;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStream() {
    return stream;
  }

  public void setStream(String stream) {
    this.stream = stream;
  }

  public Double getToxicity() {
    return toxicity;
  }

  public void setToxicity(Double toxicity) {
    this.toxicity = toxicity;
  }

  public Map<String, Long> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Long> attributes) {
    this.attributes = attributes;
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;
    ClientToxic that = (ClientToxic) o;
    return Objects.equals(name, that.name) &&
           Objects.equals(type, that.type) &&
           Objects.equals(stream, that.stream) &&
           Objects.equals(toxicity, that.toxicity) &&
           Objects.equals(attributes, that.attributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, stream, toxicity, attributes);
  }
}
