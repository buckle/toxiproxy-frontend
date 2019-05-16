package toxiproxy.backup.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToxiproxyLobRepository extends CrudRepository<ToxiproxyLobEntity, Long> {
}
