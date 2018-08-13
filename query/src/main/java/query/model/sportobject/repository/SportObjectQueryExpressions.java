package query.model.sportobject.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.sportobject.QSportObjectEntity;

public final class SportObjectQueryExpressions {

    private static final QSportObjectEntity sportObject = QSportObjectEntity.sportObjectEntity;

    public static BooleanExpression nameMatches(String name) {
        return isNotDeleted().and(sportObject.name.eq(name));
    }

    public static BooleanExpression nameAndIdMatches(String name, UUID id) {
        return isNotDeleted().and(sportObject.name.eq(name)).and(sportObject.id.eq(id));
    }
}
