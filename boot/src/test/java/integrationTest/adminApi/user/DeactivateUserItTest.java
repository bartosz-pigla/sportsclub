package integrationTest.adminApi.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static query.model.user.repository.UserQueryExpressions.idMatches;
import static web.common.RequestMappings.DIRECTOR_API_USER_ACTIVATE;

import java.util.List;
import java.util.UUID;

import api.user.command.ActivateUserCommand;
import commons.ErrorCode;
import integrationTest.AbstractUserItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.user.UserType;
import web.adminApi.user.dto.UserActivationWebCommand;

public final class DeactivateUserItTest extends AbstractUserItTest {

    @Test
    @DirtiesContext
    public void shouldDeactivateWhenUserIsCreatedAndActivated() {
        UUID userId = createUser(UserType.DIRECTOR);
        commandGateway.sendAndWait(new ActivateUserCommand(userId));
        signIn("superuser", "password");

        ResponseEntity<String> deactivatedUserResponse = patch(
                DIRECTOR_API_USER_ACTIVATE,
                new UserActivationWebCommand(false),
                String.class,
                userId.toString());

        assertEquals(deactivatedUserResponse.getStatusCode(), HttpStatus.NO_CONTENT);
        assertFalse(userRepository.findOne(idMatches(userId)).get().isActivated());
    }

    @Test
    @DirtiesContext
    public void shouldNotDeactivateWhenUserNotExists() {
        signIn("superuser", "password");

        ResponseEntity<List> deactivateUserResponse = patch(
                DIRECTOR_API_USER_ACTIVATE,
                new UserActivationWebCommand(false),
                List.class,
                UUID.randomUUID().toString());

        assertEquals(deactivateUserResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotDeactivateWhenUserIsAlreadyDeactivated() {
        UUID userId = createUser(UserType.DIRECTOR);
        signIn("superuser", "password");

        ResponseEntity<List> deactivateUserResponse = patch(
                DIRECTOR_API_USER_ACTIVATE,
                new UserActivationWebCommand(false),
                List.class,
                userId.toString());

        assertEquals(deactivateUserResponse.getStatusCode(), HttpStatus.CONFLICT);

        List errors = deactivateUserResponse.getBody();
        assertField("userId", ErrorCode.ALREADY_DEACTIVATED.getCode(), errors);
        assertFalse(userRepository.findOne(idMatches(userId)).get().isActivated());
    }
}
