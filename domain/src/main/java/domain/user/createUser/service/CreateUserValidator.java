package domain.user.createUser.service;

import static org.slf4j.LoggerFactory.getLogger;
import static query.model.user.repository.UserQueryExpressions.usernameMatches;

import api.user.command.CreateUserCommand;
import domain.common.exception.AlreadyCreatedException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import query.model.user.repository.UserEntityRepository;

@Service
@AllArgsConstructor
public final class CreateUserValidator {

    private static final Logger logger = getLogger(CreateUserValidator.class);

    private UserEntityRepository userEntityRepository;

    public void validate(CreateUserCommand command) {
        if (userEntityRepository.exists(usernameMatches(command.getUsername()))) {
            logger.error("User already exists with username: {}", command.getUsername());
            throw new AlreadyCreatedException();
        }
    }
}
