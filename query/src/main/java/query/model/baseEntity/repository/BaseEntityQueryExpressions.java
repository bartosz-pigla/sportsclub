package query.model.baseEntity.repository;

import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.QBaseEntity;

public final class BaseEntityQueryExpressions {

    public static BooleanExpression isNotDeleted(QBaseEntity baseEntity) {
        return baseEntity.deleted.eq(false);
//        return QUserEntity.userEntity.id.isNotNull();
//        return QUserEntity.userEntity.isNotNull();
//        return QUserEntity.userEntity.username.isNotEmpty();
    }

    public static BooleanExpression idMatches(UUID id, QBaseEntity baseEntity) {
        return id == null ? isNotDeleted(baseEntity).and(baseEntity.id.isNotNull()) : isNotDeleted(baseEntity).and(baseEntity.id.eq(id));
//        return QUserEntity.userEntity.id.isNotNull();
//        return QUserEntity.userEntity.isNotNull();
//        return QUserEntity.userEntity.username.isNotEmpty();
    }
}
