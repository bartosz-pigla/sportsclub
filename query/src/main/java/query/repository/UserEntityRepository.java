package query.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import query.model.user.UserEntity;
import query.model.user.UserType;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID>, QuerydslPredicateExecutor {

    boolean existsByUsernameAndDeletedFalse(String username);

    Optional<UserEntity> findByUsernameAndDeletedFalse(String username);

    boolean existsByIdAndUserTypeAndDeletedFalse(UUID id, UserType userType);
}
