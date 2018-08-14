package query.model.baseEntity.repository;

import java.util.Optional;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.QBaseEntity;

public final class BaseEntityQueryExpressions {

    public static BooleanExpression isNotDeleted(QBaseEntity baseEntity) {
        return baseEntity.deleted.eq(false);
    }

    public static BooleanExpression idMatches(UUID id, QBaseEntity baseEntity) {
        return Optional.ofNullable(id)
                .map(i -> isNotDeleted(baseEntity).and(baseEntity.id.eq(id)))
                .orElse(baseEntity.isNull());
    }
}
