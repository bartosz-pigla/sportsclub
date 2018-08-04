package query.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import query.model.sportobject.OpeningTimeEntity;

public interface OpeningTimeEntityRepository extends JpaRepository<OpeningTimeEntity, UUID> {

}
