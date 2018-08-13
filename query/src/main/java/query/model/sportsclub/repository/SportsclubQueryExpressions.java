package query.model.sportsclub.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.sportsclub.QSportsclubEntity;

public final class SportsclubQueryExpressions {

    public static BooleanExpression nameMatches(String name) {
        return isNotDeleted().and(QSportsclubEntity.sportsclubEntity.name.eq(name));
    }
}
