package integrationTest.user.staff;

import static web.common.RequestMappings.ADMIN_CONSOLE_DIRECTOR;

import integrationTest.user.AbstractUserItTest;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public final class CreateDirectorItTest extends AbstractUserItTest {

    @Test
    @DirtiesContext
    public void shouldSendErrorMessageWhenCreateDirectorIsEmpty() {
        signIn("superuser", "password");
        shouldSendErrorMessageWhenCreateUserIsEmpty(ADMIN_CONSOLE_DIRECTOR);
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveDirectorWhenDirectorWithGivenUsernameAlreadyExists() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(ADMIN_CONSOLE_DIRECTOR);
        shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(ADMIN_CONSOLE_DIRECTOR);
    }

    @Test
    @DirtiesContext
    public void shouldSaveDirectorWhenAllFieldsAreValid() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(ADMIN_CONSOLE_DIRECTOR);
    }
}
