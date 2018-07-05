package query.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import query.model.announcement.BaseAnnouncementEntity;

public interface AnnouncementEntityRepository extends CrudRepository<BaseAnnouncementEntity, UUID> {

}
