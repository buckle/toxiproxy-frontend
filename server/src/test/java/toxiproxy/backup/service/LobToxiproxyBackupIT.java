package toxiproxy.backup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toxiproxy.backup.entity.ToxiproxyLobRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LobToxiproxyBackupIT {

  @Autowired private LobToxiproxyBackupService lobToxiproxyBackupService;
  @Autowired private ToxiproxyLobRepository toxiproxyLobRepository;

  @BeforeEach
  void setUp() {
    toxiproxyLobRepository.deleteAll();
  }

  @Test
  void setBackupWhenNoneExists() {
    assertNull(lobToxiproxyBackupService.getCurrentBackup());
    LobToxiproxyBackup lobToxiproxyBackup = new LobToxiproxyBackup();
    lobToxiproxyBackup.setData(UUID.randomUUID().toString());

    lobToxiproxyBackupService.setBackup(lobToxiproxyBackup);

    LobToxiproxyBackup currentBackup = (LobToxiproxyBackup) lobToxiproxyBackupService.getCurrentBackup();
    assertNotNull(currentBackup);
    assertEquals(lobToxiproxyBackup.getData(), currentBackup.getData());
  }

  @Test
  void setBackupWhenExistingBackup() {
    LobToxiproxyBackup lobToxiproxyBackup = new LobToxiproxyBackup();
    lobToxiproxyBackup.setData(UUID.randomUUID().toString());
    lobToxiproxyBackupService.setBackup(lobToxiproxyBackup);
    ToxiproxyBackup currentBackup = lobToxiproxyBackupService.getCurrentBackup();
    assertNotNull(currentBackup);

    String newData = UUID.randomUUID().toString();
    currentBackup.setData(newData);
    lobToxiproxyBackupService.setBackup(currentBackup);

    LobToxiproxyBackup updatedBackup = (LobToxiproxyBackup) lobToxiproxyBackupService.getCurrentBackup();
    assertNotNull(updatedBackup);
    assertEquals(newData, updatedBackup.getData());
  }
}
