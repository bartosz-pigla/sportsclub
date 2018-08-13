package query.model.baseEntity.repository;

import java.util.Optional;
import java.util.UUID;

import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import query.model.baseEntity.BaseEntity;

@NoRepositoryBean
public interface BaseEntityRepository<T extends BaseEntity> extends JpaRepository<T, UUID>, QuerydslPredicateExecutor<T> {

    Optional<UUID> findOneById(UUID id, Predicate predicate);
}
