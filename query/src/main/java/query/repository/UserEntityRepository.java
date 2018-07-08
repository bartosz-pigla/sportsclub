package query.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import query.model.user.UserEntity;

public interface UserEntityRepository extends CrudRepository<UserEntity, UUID> {

    boolean existsByUsername(String username);
    Optional<UserEntity> findByUsername(String username);
}
