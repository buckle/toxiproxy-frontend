package toxiproxy.backup;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toxiproxy.backup.service.ToxiproxyBackup;
import toxiproxy.backup.service.ToxiproxyBackupService;
import toxiproxy.client.ToxiproxyClient;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BackupCheckerTest {

  @InjectMocks private BackupChecker backupChecker;
  @Mock private ToxiproxyBackupService toxiproxyBackupService;
  @Mock private ToxiproxyClient toxiproxyClient;

  @Test
  void checkWhenChangesExistToPersist() {
    ToxiproxyBackup remoteBackup = mock(ToxiproxyBackup.class);
    when(remoteBackup.getData()).thenReturn(UUID.randomUUID().toString());
    when(toxiproxyBackupService.getBackupFromRemote()).thenReturn(remoteBackup);

    ToxiproxyBackup localBackup = mock(ToxiproxyBackup.class);
    when(toxiproxyBackupService.getCurrentBackup()).thenReturn(localBackup);

    when(toxiproxyBackupService.backupsDiffer(remoteBackup, localBackup)).thenReturn(true);

    backupChecker.check();

    ArgumentCaptor<ToxiproxyBackup> toxiproxyBackupArgumentCaptor = ArgumentCaptor.forClass(ToxiproxyBackup.class);
    verify(toxiproxyBackupService, times(1)).setBackup(toxiproxyBackupArgumentCaptor.capture());
    assertEquals(remoteBackup.getData(), toxiproxyBackupArgumentCaptor.getValue().getData());
  }

  @Test
  void checkWhenBackupNeedsRestored() {
    when(toxiproxyBackupService.getBackupFromRemote()).thenReturn(null);

    ToxiproxyBackup localBackup = mock(ToxiproxyBackup.class);
    when(localBackup.getData()).thenReturn(UUID.randomUUID().toString());
    when(toxiproxyBackupService.getCurrentBackup()).thenReturn(localBackup);

    when(toxiproxyBackupService.backupsDiffer(null, localBackup)).thenReturn(true);

    backupChecker.check();

    ArgumentCaptor<ToxiproxyBackup> toxiproxyBackupArgumentCaptor = ArgumentCaptor.forClass(ToxiproxyBackup.class);
    // TODO
  }
}
