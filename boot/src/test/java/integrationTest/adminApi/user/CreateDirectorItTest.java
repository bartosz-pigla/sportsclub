package integrationTest.adminApi.user;

import static web.common.RequestMappings.ADMIN_API_DIRECTOR;

import integrationTest.AbstractUserItTest;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public final class CreateDirectorItTest extends AbstractUserItTest {

    @Test
    @DirtiesContext
    public void shouldSendErrorMessageWhenCreateDirectorIsEmpty() {
        signIn("superuser", "password");
        shouldSendErrorMessageWhenCreateUserIsEmpty(ADMIN_API_DIRECTOR);
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveWhenDirectorWithGivenUsernameAlreadyExists() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(ADMIN_API_DIRECTOR);
        shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(ADMIN_API_DIRECTOR);
    }

    @Test
    @DirtiesContext
    public void shouldSaveWhenAllFieldsAreValid() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(ADMIN_API_DIRECTOR);
    }
}
