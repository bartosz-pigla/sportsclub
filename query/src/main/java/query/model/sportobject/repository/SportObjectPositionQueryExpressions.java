package query.model.sportobject.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.sportobject.QSportObjectPositionEntity;

public final class SportObjectPositionQueryExpressions {

    public static BooleanExpression nameMatches(String sportObjectName) {
        return isNotDeleted().and(QSportObjectPositionEntity.sportObjectPositionEntity.name.eq(sportObjectName));
    }
}
