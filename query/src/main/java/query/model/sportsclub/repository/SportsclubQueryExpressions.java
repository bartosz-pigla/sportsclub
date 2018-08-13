package query.model.sportsclub.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.sportsclub.QSportsclubEntity;

public final class SportsclubQueryExpressions {

    private static final QSportsclubEntity sportsclub = QSportsclubEntity.sportsclubEntity;

    public static BooleanExpression nameMatches(String name) {
        return isNotDeleted(sportsclub._super).and(sportsclub.name.eq(name));
    }
}
