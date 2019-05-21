package toxiproxy.backup.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toxiproxy.backup.entity.ProxyEntity;
import toxiproxy.backup.entity.ProxyEntityBuilder;
import toxiproxy.backup.entity.ProxyEntityRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProxiesToxiproxyBackupServiceIT {

  @Autowired private ProxiesToxiproxyBackupService proxiesToxiproxyBackupService;
  @Autowired private ProxyEntityRepository proxyEntityRepository;

  @AfterEach
  void tearDown() {
    proxyEntityRepository.deleteAll();
  }

  @Test
  void getCurrentBackup() {
    ProxiesBackup proxiesBackup = createProxiesBackup();
    proxiesToxiproxyBackupService.setBackup(proxiesBackup);

    ProxiesBackup currentBackup = (ProxiesBackup) proxiesToxiproxyBackupService.getCurrentBackup();

    assertNotNull(currentBackup);
    assertTrue(currentBackup.getData().containsAll(proxiesBackup.getData()));
  }

  private ProxiesBackup createProxiesBackup() {
    Set<ProxyEntity> proxyEntities = new HashSet<>();
    proxyEntities.add(ProxyEntityBuilder.builder().build());
    proxyEntities.add(ProxyEntityBuilder.builder().build());

    ProxiesBackup proxiesBackup = new ProxiesBackup();
    proxiesBackup.setData(proxyEntities);

    return proxiesBackup;
  }
}
