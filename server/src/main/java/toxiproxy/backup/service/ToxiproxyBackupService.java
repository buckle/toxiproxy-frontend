package toxiproxy.backup.service;

public interface ToxiproxyBackupService {

  ToxiproxyBackup getCurrentBackup();

  void setBackup(ToxiproxyBackup content);

  boolean backupsDiffer(ToxiproxyBackup newBackup, ToxiproxyBackup existing);

}
