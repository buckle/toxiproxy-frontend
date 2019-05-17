package toxiproxy.backup.service;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LobToxiproxyBackupTest {

  @Test
  void equals() {
    LobToxiproxyBackup lobToxiproxyBackup = new LobToxiproxyBackup();
    lobToxiproxyBackup.setData(UUID.randomUUID().toString());

    LobToxiproxyBackup lobToxiproxyBackup1 = new LobToxiproxyBackup();
    lobToxiproxyBackup1.setData(lobToxiproxyBackup.getData());

    assertTrue(lobToxiproxyBackup.equals(lobToxiproxyBackup1));
  }

  @Test
  void equalsWhenNotEqual() {
    LobToxiproxyBackup lobToxiproxyBackup = new LobToxiproxyBackup();
    lobToxiproxyBackup.setData(UUID.randomUUID().toString());

    LobToxiproxyBackup lobToxiproxyBackup1 = new LobToxiproxyBackup();
    lobToxiproxyBackup1.setData(UUID.randomUUID().toString());

    assertFalse(lobToxiproxyBackup.equals(lobToxiproxyBackup1));
  }

  @Test
  void equalsWhenValuesNull() {
    LobToxiproxyBackup lobToxiproxyBackup = new LobToxiproxyBackup();
    LobToxiproxyBackup lobToxiproxyBackup1 = new LobToxiproxyBackup();

    assertTrue(lobToxiproxyBackup.equals(lobToxiproxyBackup1));
  }

  @Test
  void equalsWhenOneValueNull() {
    LobToxiproxyBackup lobToxiproxyBackup = new LobToxiproxyBackup();
    lobToxiproxyBackup.setData(UUID.randomUUID().toString());

    LobToxiproxyBackup lobToxiproxyBackup1 = new LobToxiproxyBackup();

    assertFalse(lobToxiproxyBackup.equals(lobToxiproxyBackup1));
  }
}
