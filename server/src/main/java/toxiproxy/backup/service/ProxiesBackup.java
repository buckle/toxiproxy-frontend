package toxiproxy.backup.service;

import toxiproxy.backup.entity.ProxyEntity;

import java.util.Objects;
import java.util.Set;

public class ProxiesBackup implements ToxiproxyBackup<Set<ProxyEntity>> {

  private Set<ProxyEntity> proxyEntities;

  @Override
  public Set<ProxyEntity> getData() {
    return proxyEntities;
  }

  @Override
  public void setData(Set<ProxyEntity> data) {
    this.proxyEntities = data;
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;
    ProxiesBackup that = (ProxiesBackup) o;
    return Objects.equals(proxyEntities, that.proxyEntities);
  }
}
