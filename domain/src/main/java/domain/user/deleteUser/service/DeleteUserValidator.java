package domain.user.deleteUser.service;

import static org.slf4j.LoggerFactory.getLogger;

import domain.user.User;
import domain.user.deleteUser.exception.UserAlreadyDeletedException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class DeleteUserValidator {

    private static final Logger logger = getLogger(DeleteUserValidator.class);

    public void validate(User user) {
        if (user.isDeleted()) {
            logger.error("User already deleted with id: {}", user.getUserId());
            throw new UserAlreadyDeletedException();
        }
    }
}
