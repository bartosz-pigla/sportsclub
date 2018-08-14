package query.model.sportobject.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import java.util.Optional;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.repository.BaseEntityQueryExpressions;
import query.model.sportobject.QSportObjectPositionEntity;

public final class SportObjectPositionQueryExpressions {

    private static final QSportObjectPositionEntity sportObjectPosition =
            QSportObjectPositionEntity.sportObjectPositionEntity;

    public static BooleanExpression nameMatches(String name) {
        return Optional.ofNullable(name)
                .map(n -> isNotDeleted(sportObjectPosition._super).and(sportObjectPosition.name.eq(n)))
                .orElse(sportObjectPosition.isNull());
    }

    public static BooleanExpression idMatches(UUID id) {
        return BaseEntityQueryExpressions.idMatches(id, sportObjectPosition._super);
    }
}
