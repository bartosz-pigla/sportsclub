package domain.user.activation.customer.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.time.LocalDateTime;

import api.user.command.ActivateCustomerCommand;
import domain.common.exception.AlreadyDeletedException;
import domain.user.User;
import domain.user.activation.common.exception.AlreadyActivatedException;
import domain.user.activation.customer.exception.ActivationLinkExpiredException;
import domain.user.activation.customer.exception.ActivationLinkInvalidException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class ActivateCustomerValidator {

    private static final Logger logger = getLogger(ActivateCustomerValidator.class);

    public void validate(ActivateCustomerCommand command, User customer) {
        if (customer.isDeleted()) {
            logger.error("Customer already deleted with id: {}", customer.getUserId());
            throw new AlreadyDeletedException();
        }

        if (LocalDateTime.now().isAfter(customer.getActivationDeadline())) {
            logger.error("Activation link {} expired. Activation deadline: {}", command.getActivationKey(), customer.getActivationDeadline());
            throw new ActivationLinkExpiredException();
        }

        if (!customer.getActivationKey().equals(command.getActivationKey())) {
            logger.error("Activation link {} invalid", command.getActivationKey());
            throw new ActivationLinkInvalidException();
        }

        if (customer.isActivated()) {
            logger.error("Customer {0} already activated", customer.getUserId());
            throw new AlreadyActivatedException();
        }
    }
}
