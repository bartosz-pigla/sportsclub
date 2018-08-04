package domain.user.createUser.service;

import static org.slf4j.LoggerFactory.getLogger;

import api.user.command.CreateUserCommand;
import domain.common.exception.AlreadyCreatedException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import query.repository.UserEntityRepository;

@Service
@AllArgsConstructor
public final class CreateUserValidator {

    private static final Logger logger = getLogger(CreateUserValidator.class);

    private UserEntityRepository userEntityRepository;

    public void validate(CreateUserCommand command) {
        if (userEntityRepository.existsByUsernameAndDeletedFalse(command.getUsername())) {
            logger.error("User already exists with username: {}", command.getUsername());
            throw new AlreadyCreatedException();
        }
    }
}
