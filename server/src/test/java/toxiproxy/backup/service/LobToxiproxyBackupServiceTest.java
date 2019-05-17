package toxiproxy.backup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import toxiproxy.backup.entity.ToxiproxyLobEntity;
import toxiproxy.backup.entity.ToxiproxyLobRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LobToxiproxyBackupServiceTest {

  @InjectMocks private LobToxiproxyBackupService lobToxiproxyBackupService = spy(LobToxiproxyBackupService.class);
  @Mock private ToxiproxyLobRepository toxiproxyLobRepository;

  @Test
  void getCurrentBackup() {
    ToxiproxyLobEntity toxiproxyLobEntity = mock(ToxiproxyLobEntity.class);
    when(toxiproxyLobEntity.getData()).thenReturn(UUID.randomUUID().toString());
    doReturn(toxiproxyLobEntity).when(lobToxiproxyBackupService).getBackupEntity();

    LobToxiproxyBackup currentBackup = (LobToxiproxyBackup) lobToxiproxyBackupService.getCurrentBackup();

    assertNotNull(currentBackup);
    assertEquals(String.valueOf(toxiproxyLobEntity.getData()), currentBackup.getData());
  }

  @Test
  void getCurrentBackupWhenNullEntity() {
    doReturn(null).when(lobToxiproxyBackupService).getBackupEntity();

    LobToxiproxyBackup currentBackup = (LobToxiproxyBackup) lobToxiproxyBackupService.getCurrentBackup();

    assertNull(currentBackup);
  }

  @Test
  void getCurrentBackupWhenNoDataInEntity() {
    ToxiproxyLobEntity toxiproxyLobEntity = mock(ToxiproxyLobEntity.class);
    when(toxiproxyLobEntity.getData()).thenReturn(null);
    doReturn(toxiproxyLobEntity).when(lobToxiproxyBackupService).getBackupEntity();

    LobToxiproxyBackup currentBackup = (LobToxiproxyBackup) lobToxiproxyBackupService.getCurrentBackup();

    assertNull(currentBackup);
  }

  @Test
  void setBackupWhenNew() {
    doReturn(null).when(lobToxiproxyBackupService).getBackupEntity();
    LobToxiproxyBackup lobToxiproxyBackup = mock(LobToxiproxyBackup.class);
    when(lobToxiproxyBackup.getData()).thenReturn(UUID.randomUUID().toString());

    lobToxiproxyBackupService.setBackup(lobToxiproxyBackup);

    ArgumentCaptor<ToxiproxyLobEntity> toxiproxyLobEntityArgumentCaptor = ArgumentCaptor.forClass(ToxiproxyLobEntity.class);
    verify(toxiproxyLobRepository, times(1)).save(toxiproxyLobEntityArgumentCaptor.capture());
    assertEquals(lobToxiproxyBackup.getData(), toxiproxyLobEntityArgumentCaptor.getValue().getData());
  }

  @Test
  void setBackupWhenExisting() {
    ToxiproxyLobEntity toxiproxyLobEntity = spy(ToxiproxyLobEntity.class);
    doReturn(toxiproxyLobEntity).when(lobToxiproxyBackupService).getBackupEntity();
    LobToxiproxyBackup lobToxiproxyBackup = mock(LobToxiproxyBackup.class);
    when(lobToxiproxyBackup.getData()).thenReturn(UUID.randomUUID().toString());

    lobToxiproxyBackupService.setBackup(lobToxiproxyBackup);

    ArgumentCaptor<ToxiproxyLobEntity> toxiproxyLobEntityArgumentCaptor = ArgumentCaptor.forClass(ToxiproxyLobEntity.class);
    verify(toxiproxyLobRepository, times(1)).save(toxiproxyLobEntityArgumentCaptor.capture());
    assertEquals(toxiproxyLobEntity, toxiproxyLobEntityArgumentCaptor.getValue());
    assertEquals(lobToxiproxyBackup.getData(), toxiproxyLobEntityArgumentCaptor.getValue().getData());
  }

  @Test
  void getBackupEntity() {
    ToxiproxyLobEntity toxiproxyLobEntity = mock(ToxiproxyLobEntity.class);
    when(toxiproxyLobRepository.findAll()).thenReturn(Sets.newSet(toxiproxyLobEntity));

    ToxiproxyLobEntity backupEntity = lobToxiproxyBackupService.getBackupEntity();

    assertNotNull(backupEntity);
    assertEquals(toxiproxyLobEntity, backupEntity);
  }

  @Test
  void getBackupEntityWhenNullReturnedFromRepository() {
    when(toxiproxyLobRepository.findAll()).thenReturn(null);

    ToxiproxyLobEntity backupEntity = lobToxiproxyBackupService.getBackupEntity();

    assertNull(backupEntity);
  }

  @Test
  void getBackupEntityWhenEmptyReturnedFromRepository() {
    when(toxiproxyLobRepository.findAll()).thenReturn(Sets.newSet());

    ToxiproxyLobEntity backupEntity = lobToxiproxyBackupService.getBackupEntity();

    assertNull(backupEntity);
  }

  @Test
  void backupsDifferWhenSame() {
    LobToxiproxyBackup lobToxiproxyBackup = new LobToxiproxyBackup();
    lobToxiproxyBackup.setData(UUID.randomUUID().toString());

    LobToxiproxyBackup lobToxiproxyBackup1 = new LobToxiproxyBackup();
    lobToxiproxyBackup1.setData(lobToxiproxyBackup.getData());

    assertFalse(lobToxiproxyBackupService.backupsDiffer(lobToxiproxyBackup, lobToxiproxyBackup1));
  }

  @Test
  void backupsDifferWhenNotSame() {
    LobToxiproxyBackup lobToxiproxyBackup = new LobToxiproxyBackup();
    lobToxiproxyBackup.setData(UUID.randomUUID().toString());

    LobToxiproxyBackup lobToxiproxyBackup1 = new LobToxiproxyBackup();
    lobToxiproxyBackup1.setData(UUID.randomUUID().toString());

    assertTrue(lobToxiproxyBackupService.backupsDiffer(lobToxiproxyBackup, lobToxiproxyBackup1));
  }

  @Test
  void backupsDifferWhenBothNull() {
    assertFalse(lobToxiproxyBackupService.backupsDiffer(null, null));
  }

  @Test
  void backupsDifferWhenOneNull() {
    LobToxiproxyBackup lobToxiproxyBackup = new LobToxiproxyBackup();
    lobToxiproxyBackup.setData(UUID.randomUUID().toString());

    assertTrue(lobToxiproxyBackupService.backupsDiffer(lobToxiproxyBackup, null));
  }

  @Test
  void getBackupEntityWhenMoreThanOneReturned() {
    ToxiproxyLobEntity toxiproxyLobEntity1 = mock(ToxiproxyLobEntity.class);
    when(toxiproxyLobEntity1.getUpdateTimestamp()).thenReturn(LocalDateTime.now().minusDays(1));

    ToxiproxyLobEntity toxiproxyLobEntity2 = mock(ToxiproxyLobEntity.class);
    when(toxiproxyLobEntity2.getUpdateTimestamp()).thenReturn(LocalDateTime.now().minusDays(2));

    when(toxiproxyLobRepository.findAll()).thenReturn(Sets.newSet(toxiproxyLobEntity1, toxiproxyLobEntity2));

    ToxiproxyLobEntity backupEntity = lobToxiproxyBackupService.getBackupEntity();

    assertNotNull(backupEntity);
    assertEquals(toxiproxyLobEntity1, backupEntity);
  }
}
