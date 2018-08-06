package query.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import query.model.announcement.AnnouncementEntity;

public interface AnnouncementEntityRepository extends JpaRepository<AnnouncementEntity, UUID> {

    Page<AnnouncementEntity> findAll(Pageable pageable);
}
