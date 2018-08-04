package query.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import query.model.sportobject.SportObjectEntity;

public interface SportObjectEntityRepository extends JpaRepository<SportObjectEntity, UUID> {

    Optional<SportObjectEntity> findByNameAndDeletedFalse(String name);

    boolean existsByNameAndDeletedFalse(String name);

    boolean existsByNameAndIdIsNotAndDeletedFalse(String name, UUID id);
}
