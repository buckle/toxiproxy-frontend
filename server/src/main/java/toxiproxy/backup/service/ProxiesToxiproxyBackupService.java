package toxiproxy.backup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import toxiproxy.backup.entity.ProxyEntity;
import toxiproxy.backup.entity.ProxyEntityRepository;
import toxiproxy.backup.mapper.ClientProxyToProxyEntityMapper;
import toxiproxy.client.ToxiproxyClient;
import toxiproxy.client.dto.ClientProxy;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProxiesToxiproxyBackupService implements ToxiproxyBackupService {

  @Autowired private ProxyEntityRepository proxyEntityRepository;
  @Autowired private ToxiproxyClient toxiproxyClient;
  @Autowired private ClientProxyToProxyEntityMapper clientProxyToProxyEntityMapper;

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
    Set<ClientProxy> proxies = toxiproxyClient.getProxies();

    if(proxies != null && !proxies.isEmpty()) {
      Set<ProxyEntity> proxyEntities = clientProxyToProxyEntityMapper.mapClientProxiesToProxyEntities(proxies);

      if(proxyEntities != null && !proxyEntities.isEmpty()) {
        ProxiesBackup proxiesBackup = new ProxiesBackup();
        proxiesBackup.setData(proxyEntities);

        return proxiesBackup;
      }
    }

    return null;
  }

  @Override
  public void setBackup(ToxiproxyBackup backup) {
    if(backup != null && backup.getData() != null) {
      Set<ProxyEntity> proxyEntities = ((ProxiesBackup) backup).getData();

      if(!proxyEntities.isEmpty()) {
        proxyEntityRepository.deleteAll();
        proxyEntityRepository.saveAll(proxyEntities);
      }
    }
  }

  @Override
  public boolean backupsDiffer(ToxiproxyBackup newBackup, ToxiproxyBackup existing) {
    return false;
  }
}
