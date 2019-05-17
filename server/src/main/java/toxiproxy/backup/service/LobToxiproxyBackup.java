package toxiproxy.backup.service;

import java.util.Objects;

public class LobToxiproxyBackup implements ToxiproxyBackup<String> {

  private String data;

  @Override
  public String getData() {
    return data;
  }

  @Override
  public void setData(String data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;
    LobToxiproxyBackup that = (LobToxiproxyBackup) o;
    return Objects.equals(data, that.data);
  }
}
