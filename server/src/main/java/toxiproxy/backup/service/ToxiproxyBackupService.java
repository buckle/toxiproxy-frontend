package toxiproxy.backup.service;

public interface ToxiproxyBackupService {

  ToxiproxyBackup getCurrentBackup();

  ToxiproxyBackup getBackupFromRemote();

  void restoreBackupToRemote(ToxiproxyBackup backup);

  void setBackup(ToxiproxyBackup backup);

  boolean backupsDiffer(ToxiproxyBackup newBackup, ToxiproxyBackup existingBackup);

}
