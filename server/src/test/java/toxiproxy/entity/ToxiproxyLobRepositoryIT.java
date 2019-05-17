package toxiproxy.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toxiproxy.backup.entity.ToxiproxyLobEntity;
import toxiproxy.backup.entity.ToxiproxyLobRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ToxiproxyLobRepositoryIT {

  @Autowired private ToxiproxyLobRepository toxiproxyLobRepository;

  @BeforeEach
  void setUp() {
    toxiproxyLobRepository.deleteAll();
  }

  @Test
  void save() {
    ToxiproxyLobEntity toxiproxyLobEntity = new ToxiproxyLobEntity();
    toxiproxyLobEntity.setData(UUID.randomUUID().toString());

    ToxiproxyLobEntity updatedToxiproxyLobEntity = toxiproxyLobRepository.save(toxiproxyLobEntity);

    assertNotNull(updatedToxiproxyLobEntity);
    assertNotNull(updatedToxiproxyLobEntity.getId());
    assertEquals(toxiproxyLobEntity.getData(), updatedToxiproxyLobEntity.getData());
    assertNotNull(updatedToxiproxyLobEntity.getCreateTimestamp());
    assertNotNull(updatedToxiproxyLobEntity.getUpdateTimestamp());
  }
}
