package query.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import query.model.sportobject.SportObjectPositionEntity;

public interface SportObjectPositionEntityRepository extends CrudRepository<SportObjectPositionEntity, UUID> {

    Optional<SportObjectPositionEntity> findByNameAndDeletedFalse(String name);
}
