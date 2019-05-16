package toxiproxy.backup.service;

public class LobToxiproxyBackup implements ToxiproxyBackup {

  private String data;

  @Override
  public Object getData() {
    return data;
  }

  @Override
  public void setData(Object data) {
    this.data = (String) data;
  }

  public String getDataAsString() {
    return data;
  }
}
