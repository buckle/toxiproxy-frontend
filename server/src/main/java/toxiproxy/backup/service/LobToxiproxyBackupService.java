package toxiproxy.backup.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import toxiproxy.backup.entity.ToxiproxyLobEntity;
import toxiproxy.backup.entity.ToxiproxyLobRepository;
import toxiproxy.client.ToxiproxyClient;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LobToxiproxyBackupService implements ToxiproxyBackupService {

  @Autowired private ToxiproxyLobRepository toxiproxyLobRepository;
  @Autowired private ToxiproxyClient toxiproxyClient;

  @Override
  public ToxiproxyBackup getCurrentBackup() {
    ToxiproxyLobEntity backupEntity = getBackupEntity();

    if(backupEntity != null && backupEntity.getData() != null) {
      LobToxiproxyBackup toxiproxyBackup = new LobToxiproxyBackup();
      toxiproxyBackup.setData(backupEntity.getData());

      return toxiproxyBackup;
    }

    return null;
  }

  @Override
  public ToxiproxyBackup constructBackupFromRemote() {
    String proxiesRawString = toxiproxyClient.getProxiesRawString();

    if(StringUtils.isNotEmpty(proxiesRawString)) {
      LobToxiproxyBackup lobToxiproxyBackup = new LobToxiproxyBackup();
      lobToxiproxyBackup.setData(proxiesRawString);

      return lobToxiproxyBackup;
    }

    return null;
  }

  @Override
  public void setBackup(ToxiproxyBackup toxiproxyBackup) {
    ToxiproxyLobEntity backupEntity = getBackupEntity();
    backupEntity = backupEntity == null ? new ToxiproxyLobEntity() : backupEntity;
    backupEntity.setData(((LobToxiproxyBackup) toxiproxyBackup).getData());

    toxiproxyLobRepository.save(backupEntity);
  }

  @Override
  public boolean backupsDiffer(ToxiproxyBackup newBackup, ToxiproxyBackup existingBackup) {
    if(newBackup == null && existingBackup == null) {
      return false;
    } else if(newBackup == null || existingBackup == null) {
      return true;
    }

    return !newBackup.equals(existingBackup);
  }

  protected ToxiproxyLobEntity getBackupEntity() {
    Iterable<ToxiproxyLobEntity> toxiproxyLobEntities = toxiproxyLobRepository.findAll();

    if(toxiproxyLobEntities != null && toxiproxyLobEntities.iterator().hasNext()) {
      List<ToxiproxyLobEntity> toxiproxyLobEntityList = StreamSupport.stream(toxiproxyLobEntities.spliterator(), false)
                                                                     .sorted(Comparator.comparing(ToxiproxyLobEntity::getUpdateTimestamp).reversed())
                                                                     .collect(Collectors.toList());

      return toxiproxyLobEntityList.get(0);
    }

    return null;
  }
}
