package query.model.sportsclub.repository;

import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.repository.BaseEntityQueryExpressions;
import query.model.sportsclub.QStatuteEntity;

public final class StatuteQueryExpressions {

    private static final QStatuteEntity statute = QStatuteEntity.statuteEntity;

    public static BooleanExpression idMatches(UUID id) {
        return BaseEntityQueryExpressions.idMatches(id, statute._super);
    }
}
