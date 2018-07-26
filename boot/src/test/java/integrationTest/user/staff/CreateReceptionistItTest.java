package integrationTest.user.staff;

import static web.common.RequestMappings.ADMIN_CONSOLE_RECEPTIONIST;
import static web.common.RequestMappings.SIGN_UP;

import integrationTest.user.AbstractUserItTest;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public class CreateReceptionistItTest extends AbstractUserItTest {

    @Test
    @DirtiesContext
    public void shouldSendErrorMessageWhenCreateReceptionistIsEmpty() {
        signIn("superuser", "password");
        shouldSendErrorMessageWhenCreateUserIsEmpty(ADMIN_CONSOLE_RECEPTIONIST);
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveReceptionistWhenReceptionistWithGivenUsernameAlreadyExists() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(ADMIN_CONSOLE_RECEPTIONIST);
        shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(SIGN_UP);
    }

    @Test
    @DirtiesContext
    public void shouldSaveReceptionistWhenAllFieldsAreValid() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(ADMIN_CONSOLE_RECEPTIONIST);
    }
}
