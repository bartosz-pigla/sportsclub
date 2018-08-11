package query.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import query.model.sportobject.SportObjectPositionEntity;

public interface SportObjectPositionEntityRepository extends JpaRepository<SportObjectPositionEntity, UUID> {

    Optional<SportObjectPositionEntity> findByNameAndDeletedFalse(String name);
}
