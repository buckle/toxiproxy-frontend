package toxiproxy.backup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toxiproxy.backup.service.ToxiproxyBackupService;

@Component
public class BackupChecker {

  @Autowired private ToxiproxyBackupService toxiproxyBackupService;

  public void check() {
    // Get remote proxies from toxiproxy service
    // Get local backup
    // If both empty or null, do nothing
    // If remote proxies empty and local backup not null re-populate
    // If remote proxies not empty and local backup null, save backup
    // If remote proxies not empty and local backup has no changes compared to remote, do nothing
    // If remote proxies not empty and local backup is different than remote, update backup



  }

}
