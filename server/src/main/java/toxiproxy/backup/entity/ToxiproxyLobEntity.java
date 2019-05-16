package toxiproxy.backup.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Entity
public class ToxiproxyLobEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Lob
  @Column
  private String data;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createTimestamp;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updateTimestamp;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public LocalDateTime getCreateTimestamp() {
    return createTimestamp;
  }

  public LocalDateTime getUpdateTimestamp() {
    return updateTimestamp;
  }

}
