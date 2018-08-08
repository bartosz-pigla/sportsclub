package query.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import query.model.user.UserEntity;
import query.model.user.UserType;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByUsernameAndDeletedFalse(String username);

    Optional<UserEntity> findByUsernameAndDeletedFalse(String username);

    boolean existsByIdAndUserTypeAndDeletedFalse(UUID id, UserType userType);
}
