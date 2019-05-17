package toxiproxy.backup;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toxiproxy.backup.service.ToxiproxyBackup;
import toxiproxy.backup.service.ToxiproxyBackupService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BackupCheckerTest {

  @InjectMocks private BackupChecker backupChecker;
  @Mock private ToxiproxyBackupService toxiproxyBackupService;

  @Test
  void backupCheckingWhenChangesExist() {
    ToxiproxyBackup remoteBackup = mock(ToxiproxyBackup.class);
    when(toxiproxyBackupService.constructBackupFromRemote()).thenReturn(remoteBackup);

    ToxiproxyBackup localBackup = mock(ToxiproxyBackup.class);
    when(toxiproxyBackupService.getCurrentBackup()).thenReturn(localBackup);

    when(toxiproxyBackupService.backupsDiffer(remoteBackup, localBackup)).thenReturn(true);

    backupChecker.check();

  }
}
