package query.model.user.repository;

import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.user.QActivationLinkEntry;
import query.model.user.QUserEntity;

public final class ActivationLinkQueryExpressions {

    private static final QUserEntity customerActivationLink = QActivationLinkEntry.activationLinkEntry.customer;

    public static BooleanExpression customerIdMatches(UUID customerId) {
        return customerActivationLink.id.eq(customerId);
    }

    public static BooleanExpression customerNameMatches(String customerName) {
        return customerActivationLink.username.eq(customerName);
    }
}
