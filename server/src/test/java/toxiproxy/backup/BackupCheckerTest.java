package toxiproxy.backup;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toxiproxy.backup.service.ToxiproxyBackup;
import toxiproxy.backup.service.ToxiproxyBackupService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BackupCheckerTest {

  @InjectMocks private BackupChecker backupChecker;
  @Mock private ToxiproxyBackupService toxiproxyBackupService;

  @Test
  void checkWhenChangesExistToPersist() {
    ToxiproxyBackup remoteBackup = mock(ToxiproxyBackup.class);
    when(toxiproxyBackupService.getBackupFromRemote()).thenReturn(remoteBackup);

    ToxiproxyBackup localBackup = mock(ToxiproxyBackup.class);
    when(toxiproxyBackupService.getCurrentBackup()).thenReturn(localBackup);

    when(toxiproxyBackupService.backupsDiffer(remoteBackup, localBackup)).thenReturn(true);

    backupChecker.check();

    ArgumentCaptor<ToxiproxyBackup> toxiproxyBackupArgumentCaptor = ArgumentCaptor.forClass(ToxiproxyBackup.class);
    verify(toxiproxyBackupService, times(1)).setBackup(toxiproxyBackupArgumentCaptor.capture());
    assertEquals(remoteBackup, toxiproxyBackupArgumentCaptor.getValue());
    verify(toxiproxyBackupService, never()).restoreBackupToRemote(any());
  }

  @Test
  void checkWhenBackupNeedsRestored() {
    when(toxiproxyBackupService.getBackupFromRemote()).thenReturn(null);

    ToxiproxyBackup localBackup = mock(ToxiproxyBackup.class);
    when(toxiproxyBackupService.getCurrentBackup()).thenReturn(localBackup);

    when(toxiproxyBackupService.backupsDiffer(null, localBackup)).thenReturn(true);

    backupChecker.check();

    ArgumentCaptor<ToxiproxyBackup> toxiproxyBackupArgumentCaptor = ArgumentCaptor.forClass(ToxiproxyBackup.class);
    verify(toxiproxyBackupService, times(1)).restoreBackupToRemote(toxiproxyBackupArgumentCaptor.capture());
    assertEquals(localBackup, toxiproxyBackupArgumentCaptor.getValue());
    verify(toxiproxyBackupService, never()).setBackup(any());
  }

  @Test
  void checkWhenBackupsDoNoDiffer() {
    ToxiproxyBackup remoteBackup = mock(ToxiproxyBackup.class);
    when(toxiproxyBackupService.getBackupFromRemote()).thenReturn(remoteBackup);

    ToxiproxyBackup localBackup = mock(ToxiproxyBackup.class);
    when(toxiproxyBackupService.getCurrentBackup()).thenReturn(localBackup);

    when(toxiproxyBackupService.backupsDiffer(remoteBackup, localBackup)).thenReturn(false);

    backupChecker.check();

    verify(toxiproxyBackupService, never()).setBackup(any());
    verify(toxiproxyBackupService, never()).restoreBackupToRemote(any());
  }
}
