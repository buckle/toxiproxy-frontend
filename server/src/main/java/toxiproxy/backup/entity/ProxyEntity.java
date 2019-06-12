package toxiproxy.backup.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class ProxyEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String listen;

  @Column(nullable = false)
  private String upstream;

  @Column(nullable = false)
  private boolean enabled;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createTimestamp;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getListen() {
    return listen;
  }

  public void setListen(String listen) {
    this.listen = listen;
  }

  public String getUpstream() {
    return upstream;
  }

  public void setUpstream(String upstream) {
    this.upstream = upstream;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public LocalDateTime getCreateTimestamp() {
    return createTimestamp;
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;
    ProxyEntity that = (ProxyEntity) o;
    return enabled == that.enabled &&
           Objects.equals(name, that.name) &&
           Objects.equals(listen, that.listen) &&
           Objects.equals(upstream, that.upstream);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, listen, upstream, enabled);
  }
}
