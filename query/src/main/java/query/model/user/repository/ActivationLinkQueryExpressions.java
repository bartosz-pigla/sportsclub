package query.model.user.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import java.util.Optional;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.repository.BaseEntityQueryExpressions;
import query.model.user.QActivationLinkEntry;
import query.model.user.QUserEntity;

public final class ActivationLinkQueryExpressions {

    private static final QActivationLinkEntry activationLink = QActivationLinkEntry.activationLinkEntry;
    private static final QUserEntity customerActivationLink = activationLink.customer;

    public static BooleanExpression customerIdMatches(UUID customerId) {
        return Optional.ofNullable(customerId)
                .map(c -> isNotDeleted(customerActivationLink._super).and(customerActivationLink.id.eq(c)))
                .orElse(activationLink.isNull());
    }

    public static BooleanExpression customerNameMatches(String customerName) {
        return Optional.ofNullable(customerName)
                .map(c -> isNotDeleted(customerActivationLink._super).and(customerActivationLink.username.eq(c)))
                .orElse(activationLink.isNull());
    }

    public static BooleanExpression idMatches(UUID id) {
        return BaseEntityQueryExpressions.idMatches(id, activationLink._super);
    }
}
