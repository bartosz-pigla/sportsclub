package query.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import query.model.user.ActivationLinkEntry;
import query.model.user.UserEntity;

public interface ActivationLinkEntryRepository extends CrudRepository<ActivationLinkEntry, UUID> {

    ActivationLinkEntry findByCustomerAndDeletedFalse(UserEntity customer);
}
