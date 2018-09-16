package query.model.sportobject.repository;

import java.util.Optional;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.repository.BaseEntityQueryExpressions;
import query.model.embeddable.OpeningTimeRange;
import query.model.sportobject.QOpeningTimeEntity;

public final class OpeningTimeQueryExpressions {

    private static final QOpeningTimeEntity openingTime = QOpeningTimeEntity.openingTimeEntity;

    public static BooleanExpression idMatches(UUID id) {
        return BaseEntityQueryExpressions.idMatches(id, openingTime._super);
    }

    public static BooleanExpression sportObjectIdAndTimeRangeMatches(UUID objectId, OpeningTimeRange timeRange) {
        if (objectId != null && timeRange != null) {
            return openingTime.sportObject.id.eq(objectId).and(openingTime.timeRange.eq(timeRange));
        } else {
            return openingTime.isNull();
        }
    }

    public static BooleanExpression sportObjectIdMatches(UUID objectId) {
        return Optional.ofNullable(objectId)
                .map(n -> BaseEntityQueryExpressions.isNotDeleted(openingTime._super)
                        .and(BaseEntityQueryExpressions.idMatches(objectId, openingTime.sportObject._super)))
                .orElse(openingTime.isNull());
    }
}
