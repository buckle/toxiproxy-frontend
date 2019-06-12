package toxiproxy.backup.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProxyEntityRepository extends CrudRepository<ProxyEntity, Long> {
}
