package integrationTest.adminApi.user;

import static web.common.RequestMappings.DIRECTOR_API_CUSTOMER;

import integrationTest.AbstractUserItTest;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public final class CreateCustomerItTest extends AbstractUserItTest {

    @Test
    @DirtiesContext
    public void shouldSendErrorMessageWhenCreateCustomerIsEmpty() {
        signIn("superuser", "password");
        shouldSendErrorMessageWhenCreateUserIsEmpty(DIRECTOR_API_CUSTOMER);
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveWhenCustomerWithGivenUsernameAlreadyExists() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(DIRECTOR_API_CUSTOMER);
        shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(DIRECTOR_API_CUSTOMER);
    }

    @Test
    @DirtiesContext
    public void shouldSaveWhenAllFieldsAreValid() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(DIRECTOR_API_CUSTOMER);
    }
}
