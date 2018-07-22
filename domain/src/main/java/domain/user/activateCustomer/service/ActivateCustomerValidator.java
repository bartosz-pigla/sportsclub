package domain.user.activateCustomer.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.time.LocalDateTime;

import api.user.command.ActivateCustomerCommand;
import domain.user.User;
import domain.user.activateCustomer.exception.ActivationKeyInvalidException;
import domain.user.activateCustomer.exception.ActivationLinkExpiredException;
import domain.user.activateCustomer.exception.AlreadyActivatedException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class ActivateCustomerValidator {

    private static final Logger logger = getLogger(ActivateCustomerValidator.class);

    public void validate(ActivateCustomerCommand command, User customer) {
        if (LocalDateTime.now().isAfter(customer.getActivationDeadline())) {
            logger.error("Activation link {} expired. Activation deadline: {}", command.getActivationKey(), customer.getActivationDeadline());
            throw new ActivationLinkExpiredException();
        }
        if (!customer.getActivationKey().equals(command.getActivationKey())) {
            logger.error("Activation link {} invalid", command.getActivationKey());
            throw new ActivationKeyInvalidException();
        }
        if (customer.isActivated()) {
            logger.error("Customer {0} already activated", customer.getUserId());
            throw new AlreadyActivatedException();
        }
    }
}
