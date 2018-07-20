package query.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import query.model.user.UserEntity;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);
}
