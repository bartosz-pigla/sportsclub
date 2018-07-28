package integrationTest.adminApi.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.ADMIN_CONSOLE_USER_ACTIVATION;

import java.util.List;

import api.user.command.ActivateUserCommand;
import api.user.command.CreateUserCommand;
import api.user.command.DeactivateUserCommand;
import commons.ErrorCode;
import integrationTest.AbstractUserItTest;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserEntity;
import query.model.user.UserType;
import query.repository.UserEntityRepository;
import web.adminApi.user.dto.UserActivationWebCommand;
import web.common.dto.UserDto;

public final class DeactivateUserItTest extends AbstractUserItTest {

    @Autowired
    private CommandGateway commandGateway;
    @Autowired
    private UserEntityRepository userRepository;

    @Test
    @DirtiesContext
    public void shouldDeactivateWhenUserIsCreatedAndActivated() {
        CreateUserCommand createCommand = createUser();
        activateUser(createCommand);
        signIn("superuser", "password");

        ResponseEntity<UserDto> deactivatedUserResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_USER_ACTIVATION,
                UserActivationWebCommand.builder()
                        .username(createCommand.getUsername())
                        .activated(false).build(),
                UserDto.class);

        assertEquals(deactivatedUserResponse.getStatusCode(), HttpStatus.OK);

        UserDto responseBody = deactivatedUserResponse.getBody();
        assertEquals(createCommand.getEmail().getEmail(), responseBody.getEmail());
        assertEquals(createCommand.getPhoneNumber().getPhoneNumber(), responseBody.getPhoneNumber());
        assertEquals(createCommand.getUsername(), responseBody.getUsername());
        assertEquals(createCommand.getUserType().name(), responseBody.getUserType());

        assertFalse(userRepository.findByUsername(createCommand.getUsername()).get().isActivated());
    }

    @Test
    @DirtiesContext
    public void shouldNotDeactivateWhenUserNotExists() {
        signIn("superuser", "password");

        ResponseEntity<List> deactivateUserResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_USER_ACTIVATION,
                UserActivationWebCommand.builder()
                        .username("notExistingUsername")
                        .activated(false).build(),
                List.class);

        assertEquals(deactivateUserResponse.getStatusCode(), HttpStatus.CONFLICT);

        List errors = deactivateUserResponse.getBody();
        assertField("username", ErrorCode.NOT_EXISTS.getCode(), errors);
    }

    @Test
    @DirtiesContext
    public void shouldNotDeactivateWhenUserIsAlreadyDeactivated() {
        CreateUserCommand createCommand = createUser();
        signIn("superuser", "password");

        ResponseEntity<List> deactivateUserResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_USER_ACTIVATION,
                UserActivationWebCommand.builder()
                        .username(createCommand.getUsername())
                        .activated(false).build(),
                List.class);

        assertEquals(deactivateUserResponse.getStatusCode(), HttpStatus.CONFLICT);

        List errors = deactivateUserResponse.getBody();
        assertField("username", ErrorCode.ALREADY_DEACTIVATED.getCode(), errors);

        assertFalse(userRepository.findByUsername(createCommand.getUsername()).get().isActivated());

    }

    private CreateUserCommand createUser() {
        CreateUserCommand createCommand = CreateUserCommand.builder()
                .username("username")
                .password("password")
                .userType(UserType.DIRECTOR)
                .email(new Email("bartek21@wp.pl"))
                .phoneNumber(new PhoneNumber("+48664220504")).build();
        commandGateway.sendAndWait(createCommand);
        return createCommand;
    }

    private void activateUser(CreateUserCommand createCommand) {
        UserEntity user = userRepository.findByUsername(createCommand.getUsername()).get();
        ActivateUserCommand activateCommand = ActivateUserCommand.builder()
                .userId(user.getId()).build();
        commandGateway.sendAndWait(activateCommand);
    }

    private void deactivateUser(CreateUserCommand createCommand) {
        UserEntity user = userRepository.findByUsername(createCommand.getUsername()).get();
        DeactivateUserCommand activateCommand = DeactivateUserCommand.builder()
                .userId(user.getId()).build();
        commandGateway.sendAndWait(activateCommand);
    }
}
