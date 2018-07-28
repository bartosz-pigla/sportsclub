package integrationTest.adminApi.user;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static web.common.RequestMappings.ADMIN_CONSOLE_USER_ACTIVATION;

import java.util.List;

import api.user.command.ActivateUserCommand;
import api.user.command.CreateUserCommand;
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

public final class ActivateUserItTest extends AbstractUserItTest {

    @Autowired
    private CommandGateway commandGateway;
    @Autowired
    private UserEntityRepository userRepository;

    @Test
    @DirtiesContext
    public void shouldActivateWhenUserIsCreated() {
        CreateUserCommand createCommand = createUser();
        signIn("superuser", "password");

        ResponseEntity<UserDto> activateUserResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_USER_ACTIVATION,
                UserActivationWebCommand.builder()
                        .username(createCommand.getUsername())
                        .activated(true).build(),
                UserDto.class);

        assertEquals(activateUserResponse.getStatusCode(), HttpStatus.OK);

        UserDto responseBody = activateUserResponse.getBody();
        assertEquals(createCommand.getEmail().getEmail(), responseBody.getEmail());
        assertEquals(createCommand.getPhoneNumber().getPhoneNumber(), responseBody.getPhoneNumber());
        assertEquals(createCommand.getUsername(), responseBody.getUsername());
        assertEquals(createCommand.getUserType().name(), responseBody.getUserType());

        assertTrue(userRepository.findByUsername(createCommand.getUsername()).get().isActivated());
    }

    @Test
    @DirtiesContext
    public void shouldNotActivateWhenUserNotExists() {
        signIn("superuser", "password");

        ResponseEntity<List> activateUserResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_USER_ACTIVATION,
                UserActivationWebCommand.builder()
                        .username("notExistingUsername")
                        .activated(true).build(),
                List.class);

        assertEquals(activateUserResponse.getStatusCode(), HttpStatus.CONFLICT);

        List errors = activateUserResponse.getBody();
        assertField("username", ErrorCode.NOT_EXISTS.getCode(), errors);
    }

    @Test
    @DirtiesContext
    public void shouldNotActivateWhenUserIsAlreadyActivated() {
        CreateUserCommand createCommand = createUser();
        activateUser(createCommand);
        signIn("superuser", "password");

        ResponseEntity<List> activateUserResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_USER_ACTIVATION,
                UserActivationWebCommand.builder()
                        .username(createCommand.getUsername())
                        .activated(true).build(),
                List.class);

        assertEquals(activateUserResponse.getStatusCode(), HttpStatus.CONFLICT);

        List errors = activateUserResponse.getBody();
        assertField("username", ErrorCode.ALREADY_ACTIVATED.getCode(), errors);

        assertTrue(userRepository.findByUsername(createCommand.getUsername()).get().isActivated());

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
}
