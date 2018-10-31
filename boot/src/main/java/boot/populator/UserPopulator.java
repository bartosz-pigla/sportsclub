package boot.populator;

import static query.model.user.repository.UserQueryExpressions.usernameMatches;

import api.user.command.ActivateUserCommand;
import api.user.command.CreateUserCommand;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserEntity;
import query.model.user.UserType;
import query.model.user.repository.UserEntityRepository;

@Service
@AllArgsConstructor
public final class UserPopulator {

    private CommandGateway commandGateway;
    private UserEntityRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public void initializeDirector() {
        String username = "superuser";
        if (!userRepository.exists(usernameMatches(username))) {
            commandGateway.sendAndWait(CreateUserCommand.builder()
                    .username(username)
                    .password(passwordEncoder.encode("password"))
                    .userType(UserType.DIRECTOR)
                    .email(new Email("bartek217a@wp.pl"))
                    .phoneNumber(new PhoneNumber("+48664220607"))
                    .build());
            UserEntity user = userRepository.findOne(usernameMatches(username)).get();
            commandGateway.sendAndWait(new ActivateUserCommand(user.getId()));
        }
    }

    public void initializeReceptionist() {
        String username = "receptionist";
        if (!userRepository.exists(usernameMatches(username))) {
            commandGateway.sendAndWait(CreateUserCommand.builder()
                    .username(username)
                    .password(passwordEncoder.encode("password"))
                    .userType(UserType.RECEPTIONIST)
                    .email(new Email("receptionist@wp.pl"))
                    .phoneNumber(new PhoneNumber("+48664220607"))
                    .build());
            UserEntity user = userRepository.findOne(usernameMatches(username)).get();
            commandGateway.sendAndWait(new ActivateUserCommand(user.getId()));
        }
    }

    public void initializeCustomer() {
        String username = "customer";
        if (!userRepository.exists(usernameMatches(username))) {
            commandGateway.sendAndWait(CreateUserCommand.builder()
                    .username(username)
                    .password(passwordEncoder.encode("password"))
                    .userType(UserType.CUSTOMER)
                    .email(new Email("customer@wp.pl"))
                    .phoneNumber(new PhoneNumber("+48123456789"))
                    .build());
            UserEntity user = userRepository.findOne(usernameMatches(username)).get();
            commandGateway.sendAndWait(new ActivateUserCommand(user.getId()));
        }
    }
}
