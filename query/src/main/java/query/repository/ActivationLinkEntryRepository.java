package query.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import query.model.user.ActivationLinkEntry;

public interface ActivationLinkEntryRepository extends CrudRepository<ActivationLinkEntry, UUID> {

}
