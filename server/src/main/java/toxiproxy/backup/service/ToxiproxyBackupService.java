package toxiproxy.backup.service;

public interface ToxiproxyBackupService {

  ToxiproxyBackup getCurrentBackup();

  ToxiproxyBackup constructBackupFromRemote();

  void setBackup(ToxiproxyBackup content);

  boolean backupsDiffer(ToxiproxyBackup newBackup, ToxiproxyBackup existing);

}
