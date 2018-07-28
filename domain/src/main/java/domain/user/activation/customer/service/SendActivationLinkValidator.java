package domain.user.activation.customer.service;

import static org.slf4j.LoggerFactory.getLogger;

import domain.common.exception.AlreadyDeletedException;
import domain.user.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class SendActivationLinkValidator {

    private static final Logger logger = getLogger(ActivateCustomerValidator.class);

    public void validate(User customer) {
        if (customer.isDeleted()) {
            logger.error("Customer already deleted with id: {}", customer.getUserId());
            throw new AlreadyDeletedException();
        }
    }
}
