package domain.user.activation.user.service;

import static org.slf4j.LoggerFactory.getLogger;

import api.user.command.ActivateUserCommand;
import domain.common.exception.AlreadyDeletedException;
import domain.user.User;
import domain.user.activation.common.exception.AlreadyActivatedException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public final class ActivateUserValidator {

    private static final Logger logger = getLogger(ActivateUserValidator.class);

    public void validate(ActivateUserCommand command, User user) {
        if (user.isDeleted()) {
            logger.error("User with id: {} already deleted", user.getUserId());
            throw new AlreadyDeletedException();
        }

        if (user.isActivated()) {
            logger.error("User with id: {} already activated", user.getUserId());
            throw new AlreadyActivatedException();
        }
    }
}
