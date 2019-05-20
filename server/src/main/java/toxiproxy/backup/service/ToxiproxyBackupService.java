package toxiproxy.backup.service;

public interface ToxiproxyBackupService {

  ToxiproxyBackup getCurrentBackup();

  ToxiproxyBackup getBackupFromRemote();

  void setBackup(ToxiproxyBackup content);

  boolean backupsDiffer(ToxiproxyBackup newBackup, ToxiproxyBackup existing);

}
