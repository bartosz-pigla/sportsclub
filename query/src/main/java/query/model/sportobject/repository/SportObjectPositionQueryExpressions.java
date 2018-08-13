package query.model.sportobject.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.sportobject.QSportObjectPositionEntity;

public final class SportObjectPositionQueryExpressions {

    private static final QSportObjectPositionEntity sportObjectPosition =
            QSportObjectPositionEntity.sportObjectPositionEntity;

    public static BooleanExpression nameMatches(String sportObjectName) {
        return isNotDeleted(sportObjectPosition._super).and(sportObjectPosition.name.eq(sportObjectName));
    }
}
