package query.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import query.model.user.UserEntity;

public interface UserEntityRepository extends CrudRepository<UserEntity, UUID> {

}
