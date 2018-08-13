package web.adminApi.user.service;

import api.user.command.CreateUserCommand;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserType;
import web.common.user.dto.CreateUserWebCommand;

@Service
@AllArgsConstructor
public final class CreateUserService {

    private CommandGateway commandGateway;
    private PasswordEncoder passwordEncoder;

    public void create(CreateUserWebCommand user, UserType userType) {
        commandGateway.sendAndWait(CreateUserCommand.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(new Email(user.getEmail()))
                .phoneNumber(new PhoneNumber(user.getPhoneNumber()))
                .userType(userType).build());
    }

}
