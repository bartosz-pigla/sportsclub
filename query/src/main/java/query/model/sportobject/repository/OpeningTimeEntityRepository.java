package query.model.sportobject.repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import query.model.baseEntity.repository.BaseEntityRepository;
import query.model.sportobject.OpeningTimeEntity;
import query.model.sportobject.PositionsWithOpeningTimes;

public interface OpeningTimeEntityRepository extends BaseEntityRepository<OpeningTimeEntity> {

    @Query("SELECT new query.model.sportobject.PositionsWithOpeningTimes(p.id, p.name, p.positionsCount, o.id, o.timeRange.startTime, o.timeRange.finishTime, o.price) FROM OpeningTimeEntity o, SportObjectPositionEntity p WHERE o.deleted = FALSE AND p.deleted = FALSE AND o.sportObject.id=p.sportObject.id AND o.sportObject.id=?1 AND o.timeRange.dayOfWeek=?2 ORDER BY o.timeRange.startTime, p.name")
    List<PositionsWithOpeningTimes> getPositionsWithOpeningTimes(UUID sportObjectId, DayOfWeek dayOfWeek);
}
