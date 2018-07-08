package domain.user;

import api.user.command.CreateUserCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationException extends RuntimeException {

    private CreateUserCommand command;
}
