package integrationTest.adminApi.user;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
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
import query.model.user.repository.UserQueryExpressions;
import web.adminApi.user.dto.UserActivationWebCommand;

public final class ActivateUserItTest extends AbstractUserItTest {

    @Test
    @DirtiesContext
    public void shouldActivateWhenUserIsCreated() {
        UUID userId = createUser(UserType.DIRECTOR);
        signIn("superuser", "password");

        ResponseEntity<String> activateUserResponse = patch(
                DIRECTOR_API_USER_ACTIVATE,
                new UserActivationWebCommand(true),
                String.class,
                userId.toString());

        assertEquals(activateUserResponse.getStatusCode(), HttpStatus.NO_CONTENT);
        assertTrue(userRepository.findOne(UserQueryExpressions.idMatches(userId)).get().isActivated());
    }

    @Test
    @DirtiesContext
    public void shouldNotActivateWhenUserNotExists() {
        signIn("superuser", "password");

        ResponseEntity<String> activateUserResponse = patch(
                DIRECTOR_API_USER_ACTIVATE,
                new UserActivationWebCommand(true),
                String.class,
                "notExistingUserId");

        assertEquals(activateUserResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotActivateWhenUserIsAlreadyActivated() {
        UUID userId = createUser(UserType.DIRECTOR);
        commandGateway.sendAndWait(new ActivateUserCommand(userId));
        signIn("superuser", "password");

        ResponseEntity<List> activateUserResponse = patch(
                DIRECTOR_API_USER_ACTIVATE,
                new UserActivationWebCommand(true),
                List.class,
                userId.toString());

        assertEquals(activateUserResponse.getStatusCode(), HttpStatus.CONFLICT);

        List errors = activateUserResponse.getBody();
        assertField("userId", ErrorCode.ALREADY_ACTIVATED.getCode(), errors);
        assertTrue(userRepository.findOne(idMatches(userId)).get().isActivated());
    }
}
