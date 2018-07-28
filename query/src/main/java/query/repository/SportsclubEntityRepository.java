package query.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import query.model.sportsclub.SportsclubEntity;

public interface SportsclubEntityRepository extends CrudRepository<SportsclubEntity, UUID> {

    Optional<SportsclubEntity> findByName(String name);

    boolean existsByName(String name);
}
