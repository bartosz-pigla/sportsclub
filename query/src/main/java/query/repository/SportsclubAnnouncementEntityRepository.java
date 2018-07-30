package query.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import query.model.announcement.SportsclubAnnouncementEntity;

public interface SportsclubAnnouncementEntityRepository extends CrudRepository<SportsclubAnnouncementEntity, UUID> {

    Page<SportsclubAnnouncementEntity> findAll(Pageable pageable);
}
