package domain.user.activation.user.service;

import static org.slf4j.LoggerFactory.getLogger;

import api.user.command.DeactivateUserCommand;
import domain.common.exception.AlreadyDeletedException;
import domain.user.User;
import domain.user.activation.common.exception.AlreadyDeactivatedException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public final class DeactivateUserValidator {

    private static final Logger logger = getLogger(DeactivateUserValidator.class);

    public void validate(DeactivateUserCommand command, User user) {
        if (user.isDeleted()) {
            logger.error("User with id: {} already deleted", user.getUserId());
            throw new AlreadyDeletedException();
        }

        if (!user.isActivated()) {
            logger.error("User with id: {} already deactivated", user.getUserId());
            throw new AlreadyDeactivatedException();
        }
    }
}
