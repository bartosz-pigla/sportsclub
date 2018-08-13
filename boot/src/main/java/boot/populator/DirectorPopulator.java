package boot.populator;

import api.user.command.ActivateUserCommand;
import api.user.command.CreateUserCommand;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.QUserEntity;
import query.model.user.UserEntity;
import query.model.user.UserType;
import query.repository.UserEntityRepository;

@Service
@AllArgsConstructor
public final class DirectorPopulator {

    private CommandGateway commandGateway;
    private UserEntityRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public void initializeDirector() {
        if (!userRepository.existsByUsernameAndDeletedFalse("superuser")) {
            commandGateway.sendAndWait(CreateUserCommand.builder()
                    .username("superuser")
                    .password(passwordEncoder.encode("password"))
                    .userType(UserType.DIRECTOR)
                    .email(new Email("bartek217a@wp.pl"))
                    .phoneNumber(new PhoneNumber("+48664220607")).build());

            UserEntity user = userRepository.findByUsernameAndDeletedFalse("superuser").get();
            commandGateway.sendAndWait(ActivateUserCommand.builder()
                    .userId(user.getId()).build());
        }
    }
}
