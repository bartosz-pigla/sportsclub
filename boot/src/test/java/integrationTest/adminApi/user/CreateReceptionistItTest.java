package integrationTest.adminApi.user;

import static web.common.RequestMappings.DIRECTOR_API_RECEPTIONIST;
import static web.common.RequestMappings.PUBLIC_API_SIGN_UP;

import integrationTest.AbstractUserItTest;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public class CreateReceptionistItTest extends AbstractUserItTest {

    @Test
    @DirtiesContext
    public void shouldSendErrorMessageWhenCreateReceptionistIsEmpty() {
        signIn("superuser", "password");
        shouldSendErrorMessageWhenCreateUserIsEmpty(DIRECTOR_API_RECEPTIONIST);
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveWhenReceptionistWithGivenUsernameAlreadyExists() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(DIRECTOR_API_RECEPTIONIST);
        shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(PUBLIC_API_SIGN_UP);
    }

    @Test
    @DirtiesContext
    public void shouldSaveWhenAllFieldsAreValid() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(DIRECTOR_API_RECEPTIONIST);
    }
}
