package query.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import query.model.statute.StatuteEntity;

public interface StatuteEntityRepository extends CrudRepository<StatuteEntity, UUID> {

}
