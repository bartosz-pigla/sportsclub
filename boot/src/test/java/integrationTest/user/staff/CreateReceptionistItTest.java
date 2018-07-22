package integrationTest.user.staff;

import static web.common.RequestMappings.DIRECTOR;
import static web.common.RequestMappings.RECEPTIONIST;
import static web.common.RequestMappings.SIGN_UP;

import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public class CreateReceptionistItTest extends AbstractStaffItTest {

    @Test
    public void shouldSendErrorMessageWhenCreateReceptionistIsEmpty() {
        shouldSendErrorMessageWhenCreateUserIsEmpty(RECEPTIONIST);
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveReceptionistWhenReceptionistWithGivenUsernameAlreadyExists() {
        shouldSaveUserWhenAllFieldsAreValid(DIRECTOR);
        shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(SIGN_UP);
    }

    @Test
    @DirtiesContext
    public void shouldSaveReceptionistWhenAllFieldsAreValid() {
        shouldSaveUserWhenAllFieldsAreValid(DIRECTOR);
    }
}
