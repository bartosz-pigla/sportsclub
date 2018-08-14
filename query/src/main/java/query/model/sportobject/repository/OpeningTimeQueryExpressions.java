package query.model.sportobject.repository;

import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.repository.BaseEntityQueryExpressions;
import query.model.sportobject.QOpeningTimeEntity;

public final class OpeningTimeQueryExpressions {

    private static final QOpeningTimeEntity openingTime = QOpeningTimeEntity.openingTimeEntity;

    public static BooleanExpression idMatches(UUID id) {
        return BaseEntityQueryExpressions.idMatches(id, openingTime._super);
    }
}
