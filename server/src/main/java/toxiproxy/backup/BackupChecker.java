package toxiproxy.backup;

import net.javacrumbs.shedlock.core.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import toxiproxy.backup.service.ToxiproxyBackup;
import toxiproxy.backup.service.ToxiproxyBackupService;

@Component
public class BackupChecker {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired private ToxiproxyBackupService toxiproxyBackupService;

  @Scheduled(fixedDelayString = "${backup.cadence}")
  @SchedulerLock(name = "BACKUP_CHECKER", lockAtMostForString = "${backup.cadence}")
  public void check() {
    ToxiproxyBackup currentRemote = toxiproxyBackupService.getBackupFromRemote();
    ToxiproxyBackup currentBackup = toxiproxyBackupService.getCurrentBackup();

    if(toxiproxyBackupService.backupsDiffer(currentRemote, currentBackup)) {
      if(currentBackup != null && currentRemote == null) {
        toxiproxyBackupService.restoreBackupToRemote(currentBackup);
        logger.info("Restoring backup to Toxiproxy service.");
      } else {
        toxiproxyBackupService.setBackup(currentRemote);
        logger.info("Updating backup from changes in Toxiproxy service.");
      }
    }
  }

}
