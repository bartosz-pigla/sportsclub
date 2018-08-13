package query.model.baseEntity.repository;

import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.user.QUserEntity;

public final class BaseEntityQueryExpressions {

    public static BooleanExpression isNotDeleted() {
//        return QBaseEntity.baseEntity.deleted.eq(false);
        return QUserEntity.userEntity.id.isNotNull();
    }

    public static BooleanExpression idMatches(UUID id) {
//        ComparablePath<UUID> idComparablePath = QBaseEntity.baseEntity.id;
//        return id == null ? isNotDeleted().and(idComparablePath.isNotNull()) : isNotDeleted().and(idComparablePath.eq(id));
        return QUserEntity.userEntity.id.isNotNull();

    }
}
