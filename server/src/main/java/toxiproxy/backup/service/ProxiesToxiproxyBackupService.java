package toxiproxy.backup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import toxiproxy.backup.entity.ProxyEntity;
import toxiproxy.backup.entity.ProxyEntityRepository;
import toxiproxy.client.ToxiproxyClient;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProxiesToxiproxyBackupService implements ToxiproxyBackupService {

  @Autowired private ProxyEntityRepository proxyEntityRepository;
  @Autowired private ToxiproxyClient toxiproxyClient;

  @Override
  public ToxiproxyBackup getCurrentBackup() {
    ProxiesBackup proxiesBackup = new ProxiesBackup();
    Iterable<ProxyEntity> proxyEntityIterable = proxyEntityRepository.findAll();

    if(proxyEntityIterable != null && proxyEntityIterable.iterator().hasNext()) {
      Set<ProxyEntity> proxyEntitySet = StreamSupport.stream(proxyEntityIterable.spliterator(), false)
                                                     .collect(Collectors.toSet());
      proxiesBackup.setData(proxyEntitySet);
      return proxiesBackup;
    }

    return null;
  }

  @Override
  public ToxiproxyBackup getBackupFromRemote() {
    return null;
  }

  @Override
  public void setBackup(ToxiproxyBackup content) {

  }

  @Override
  public boolean backupsDiffer(ToxiproxyBackup newBackup, ToxiproxyBackup existing) {
    return false;
  }
}
