package toxiproxy.client.dto;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static toxiproxy.client.dto.ClientCloningUtils.createToxicClone;

public class ClientToxicTest {

  @Test
  void equalsAndHashWhenObjectsSame() {
    ClientToxic clientToxic = new ClientToxic();

    assertTrue(clientToxic.equals(clientToxic));
    assertEquals(clientToxic.hashCode(), clientToxic.hashCode());
  }

  @Test
  void equalsAndHashWhenDataSame() {
    ClientToxic clientToxic1 = ClientToxicBuilder.builder().build();
    ClientToxic clientToxic2 = createToxicClone(clientToxic1);

    assertTrue(clientToxic1.equals(clientToxic2));
    assertEquals(clientToxic1.hashCode(), clientToxic2.hashCode());
  }

  @Test
  void equalsAndHashWhenNameDifferent() {
    ClientToxic clientToxic1 = ClientToxicBuilder.builder().build();
    ClientToxic clientToxic2 = createToxicClone(clientToxic1);
    clientToxic1.setName(UUID.randomUUID().toString());

    assertFalse(clientToxic1.equals(clientToxic2));
    assertNotEquals(clientToxic1.hashCode(), clientToxic2.hashCode());
  }

  @Test
  void equalsAndHashWhenTypeDifferent() {
    ClientToxic clientToxic1 = ClientToxicBuilder.builder().build();
    ClientToxic clientToxic2 = createToxicClone(clientToxic1);
    clientToxic1.setType(UUID.randomUUID().toString());

    assertFalse(clientToxic1.equals(clientToxic2));
    assertNotEquals(clientToxic1.hashCode(), clientToxic2.hashCode());
  }

  @Test
  void equalsAndHashWhenStreamDifferent() {
    ClientToxic clientToxic1 = ClientToxicBuilder.builder().build();
    ClientToxic clientToxic2 = createToxicClone(clientToxic1);
    clientToxic1.setStream(UUID.randomUUID().toString());

    assertFalse(clientToxic1.equals(clientToxic2));
    assertNotEquals(clientToxic1.hashCode(), clientToxic2.hashCode());
  }

  @Test
  void equalsAndHashWhenToxicityDifferent() {
    ClientToxic clientToxic1 = ClientToxicBuilder.builder().build();
    ClientToxic clientToxic2 = createToxicClone(clientToxic1);
    clientToxic1.setToxicity(RandomUtils.nextDouble());

    assertFalse(clientToxic1.equals(clientToxic2));
    assertNotEquals(clientToxic1.hashCode(), clientToxic2.hashCode());
  }

  @Test
  void equalsAndHashWhenAttributesDifferent() {
    ClientToxic clientToxic1 = ClientToxicBuilder.builder().build();
    ClientToxic clientToxic2 = createToxicClone(clientToxic1);
    clientToxic1.getAttributes().put(UUID.randomUUID().toString(), RandomUtils.nextLong());

    assertFalse(clientToxic1.equals(clientToxic2));
    assertNotEquals(clientToxic1.hashCode(), clientToxic2.hashCode());
  }

}
