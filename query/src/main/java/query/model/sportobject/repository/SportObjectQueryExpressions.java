package query.model.sportobject.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import java.util.Optional;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.repository.BaseEntityQueryExpressions;
import query.model.sportobject.QSportObjectEntity;

public final class SportObjectQueryExpressions {

    private static final QSportObjectEntity sportObject = QSportObjectEntity.sportObjectEntity;

    public static BooleanExpression nameMatches(String name) {
        return Optional.ofNullable(name)
                .map(n -> isNotDeleted(sportObject._super).and(sportObject.name.eq(n)))
                .orElse(sportObject.isNull());
    }

    public static BooleanExpression nameAndSportsclubIdMatches(String name, UUID sportsclubId) {
        return Optional.ofNullable(sportsclubId)
                .map(i -> nameMatches(name).and(sportObject.headquarter.id.eq(sportsclubId)))
                .orElse(sportObject.isNull());
    }

    public static BooleanExpression nameMatchesWithIdOtherThan(String name, UUID id) {
        return Optional.ofNullable(id)
                .map(i -> nameMatches(name).and(sportObject._super.id.ne(i)))
                .orElse(sportObject.isNull());
    }

    public static BooleanExpression idMatches(UUID id) {
        return BaseEntityQueryExpressions.idMatches(id, sportObject._super);
    }
}
