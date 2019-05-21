package toxiproxy.backup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import toxiproxy.backup.service.ToxiproxyBackup;
import toxiproxy.backup.service.ToxiproxyBackupService;

@Component
public class BackupChecker {

  @Autowired private ToxiproxyBackupService toxiproxyBackupService;

  @Scheduled(fixedDelayString = "${backup.cadence}")
  public void check() {
    ToxiproxyBackup currentRemote = toxiproxyBackupService.getBackupFromRemote();
    ToxiproxyBackup currentBackup = toxiproxyBackupService.getCurrentBackup();

    if(toxiproxyBackupService.backupsDiffer(currentRemote, currentBackup)) {
      if(currentBackup != null && currentRemote == null) {
        toxiproxyBackupService.restoreBackupToRemote(currentBackup);
      } else {
        toxiproxyBackupService.setBackup(currentRemote);
      }
    }
  }

}
