package domain.user;

import java.time.LocalDateTime;

import api.user.command.ActivateCustomerCommand;
import api.user.command.CreateUserCommand;
import domain.user.exception.ActivationKeyInvalidException;
import domain.user.exception.ActivationLinkExpiredException;
import domain.user.exception.AlreadyActivatedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import query.repository.UserEntityRepository;

@Service
@AllArgsConstructor
public class UserValidator {

    private UserEntityRepository userEntityRepository;

    void validate(CreateUserCommand command) {
        if (userEntityRepository.existsByUsername(command.getUsername())) {
            throw new UserCreationException(command);
        }
    }

    void validate(ActivateCustomerCommand command, User user) {
        if (LocalDateTime.now().isAfter(user.getActivationDeadline())) {
            throw new ActivationLinkExpiredException(command);
        }
        if (!user.getActivationKey().equals(command.getActivationKey())) {
            throw new ActivationKeyInvalidException(command);
        }
        if (user.isActivated()) {
            throw new AlreadyActivatedException(command);
        }
    }
}
