package integrationTest.adminApi.user;

import static web.common.RequestMappings.DIRECTOR_API_DIRECTOR;

import integrationTest.AbstractUserItTest;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public final class CreateDirectorItTest extends AbstractUserItTest {

    @Test
    @DirtiesContext
    public void shouldSendErrorMessageWhenCreateDirectorIsEmpty() {
        signIn("superuser", "password");
        shouldSendErrorMessageWhenCreateUserIsEmpty(DIRECTOR_API_DIRECTOR);
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveWhenDirectorWithGivenUsernameAlreadyExists() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(DIRECTOR_API_DIRECTOR);
        shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(DIRECTOR_API_DIRECTOR);
    }

    @Test
    @DirtiesContext
    public void shouldSaveWhenAllFieldsAreValid() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(DIRECTOR_API_DIRECTOR);
    }
}
