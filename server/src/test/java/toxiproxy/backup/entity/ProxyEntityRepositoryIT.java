package toxiproxy.backup.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProxyEntityRepositoryIT {

  @Autowired private ProxyEntityRepository proxyEntityRepository;

  @Test
  void save() {
    ProxyEntity proxyEntity = new ProxyEntity();
    proxyEntity.setName("Name" + UUID.randomUUID().toString());
    proxyEntity.setListen("listen"+ UUID.randomUUID().toString()+":8474");
    proxyEntity.setUpstream("upstream"+ UUID.randomUUID().toString()+":8474");
    proxyEntity.setEnabled(true);

    ProxyEntity savedEntity = proxyEntityRepository.save(proxyEntity);

    assertNotNull(savedEntity);
    assertNotNull(savedEntity.getId());
    assertEquals(proxyEntity.getName(), savedEntity.getName());
    assertEquals(proxyEntity.getListen(), savedEntity.getListen());
    assertEquals(proxyEntity.getUpstream(), savedEntity.getUpstream());
    assertEquals(proxyEntity.isEnabled(), savedEntity.isEnabled());
    assertNotNull(savedEntity.getCreateTimestamp());
  }
}
