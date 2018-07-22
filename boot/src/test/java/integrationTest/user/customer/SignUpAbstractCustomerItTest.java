package integrationTest.user.customer;

import static web.common.RequestMappings.SIGN_UP;

import integrationTest.user.AbstractUserItTest;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public final class SignUpAbstractCustomerItTest extends AbstractUserItTest {

    @Test
    public void shouldSendErrorMessageWhenCreateCustomerIsEmpty() {
        shouldSendErrorMessageWhenCreateUserIsEmpty(SIGN_UP);
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveCustomerWhenUserWithGivenUsernameAlreadyExists() {
        shouldSaveCustomerWhenAllFieldsAreValid();
        shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(SIGN_UP);
    }

    @Test
    @DirtiesContext
    public void shouldSaveCustomerWhenAllFieldsAreValid() {
        shouldSaveUserWhenAllFieldsAreValid(SIGN_UP);
    }
}
