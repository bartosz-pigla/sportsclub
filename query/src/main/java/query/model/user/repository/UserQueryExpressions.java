package query.model.user.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import java.util.Optional;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.repository.BaseEntityQueryExpressions;
import query.model.user.QUserEntity;
import query.model.user.UserType;

public final class UserQueryExpressions {

    private static final QUserEntity user = QUserEntity.userEntity;

    public static BooleanExpression usernameMatches(String username) {
        return Optional.ofNullable(username)
                .map(u -> isNotDeleted(user._super).and(user.username.eq(u)))
                .orElse(user.isNull());
    }

    public static BooleanExpression idAndUserTypeMatches(UUID id, UserType userType) {
        return Optional.ofNullable(userType)
                .map(u -> idMatches(id).and(user.userType.eq(u)))
                .orElse(user.isNull());
    }

    public static BooleanExpression idMatches(UUID id) {
        return BaseEntityQueryExpressions.idMatches(id, user._super);
    }
}
