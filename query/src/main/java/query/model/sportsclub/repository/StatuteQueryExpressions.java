package query.model.sportsclub.repository;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.repository.BaseEntityQueryExpressions;
import query.model.sportsclub.QStatuteEntity;

public final class StatuteQueryExpressions {

    private static final QStatuteEntity statute = QStatuteEntity.statuteEntity;

    public static BooleanExpression idMatches(UUID id) {
        return Optional.ofNullable(id)
                .map(n -> BaseEntityQueryExpressions.idMatches(id, statute._super))
                .orElse(statute.isNull());
    }

    public static BooleanExpression sportsclubIdMatches(UUID sportsclubId) {
        return Optional.ofNullable(sportsclubId)
                .map(i -> BaseEntityQueryExpressions.idMatches(sportsclubId, statute.sportsclub._super))
                .orElse(statute.isNull());
    }

    public static BooleanExpression titleAndSportsclubIdMatches(String title, UUID sportsclubId) {
        if (isNotBlank(title) && sportsclubId != null) {
            return statute.title.eq(title).and(BaseEntityQueryExpressions.idMatches(sportsclubId, statute.sportsclub._super));
        } else {
            return statute.isNull();
        }
    }
}
