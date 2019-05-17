package toxiproxy.backup.service;

public interface ToxiproxyBackup<T> {

  T getData();

  void setData(T data);

}
