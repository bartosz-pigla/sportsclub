package query.model.user.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.idMatches;
import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.user.QUserEntity;
import query.model.user.UserType;

public final class UserQueryExpressions {

    private static final QUserEntity user = QUserEntity.userEntity;

    public static BooleanExpression usernameMatches(String username) {
        return isNotDeleted().and(user.username.eq(username));
    }

    public static BooleanExpression idAndUserTypeMatches(UUID id, UserType userType) {
        return idMatches(id).and(user.userType.eq(userType));
    }
}
