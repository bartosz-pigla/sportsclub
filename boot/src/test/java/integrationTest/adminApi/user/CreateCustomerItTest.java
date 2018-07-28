package integrationTest.adminApi.user;

import static web.common.RequestMappings.ADMIN_CONSOLE_CUSTOMER;

import integrationTest.AbstractUserItTest;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public final class CreateCustomerItTest extends AbstractUserItTest {

    @Test
    @DirtiesContext
    public void shouldSendErrorMessageWhenCreateCustomerIsEmpty() {
        signIn("superuser", "password");
        shouldSendErrorMessageWhenCreateUserIsEmpty(ADMIN_CONSOLE_CUSTOMER);
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveWhenCustomerWithGivenUsernameAlreadyExists() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(ADMIN_CONSOLE_CUSTOMER);
        shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(ADMIN_CONSOLE_CUSTOMER);
    }

    @Test
    @DirtiesContext
    public void shouldSaveWhenAllFieldsAreValid() {
        signIn("superuser", "password");
        shouldSaveUserWhenAllFieldsAreValid(ADMIN_CONSOLE_CUSTOMER);
    }
}
