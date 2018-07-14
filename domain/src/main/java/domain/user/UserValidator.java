package domain.user;

import api.user.command.CreateUserCommand;
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
}
