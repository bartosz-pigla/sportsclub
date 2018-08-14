package query.model.sportsclub.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import java.util.Optional;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.repository.BaseEntityQueryExpressions;
import query.model.sportsclub.QSportsclubEntity;

public final class SportsclubQueryExpressions {

    private static final QSportsclubEntity sportsclub = QSportsclubEntity.sportsclubEntity;

    public static BooleanExpression nameMatches(String name) {
        return Optional.ofNullable(name)
                .map(n -> isNotDeleted(sportsclub._super).and(sportsclub.name.eq(name)))
                .orElse(sportsclub.isNull());
    }

    public static BooleanExpression idMatches(UUID id) {
        return BaseEntityQueryExpressions.idMatches(id, sportsclub._super);
    }
}
