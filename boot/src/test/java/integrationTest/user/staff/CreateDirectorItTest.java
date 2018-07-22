package integrationTest.user.staff;

import static web.common.RequestMappings.DIRECTOR;

import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public final class CreateDirectorItTest extends AbstractStaffItTest {

    @Test
    public void shouldSendErrorMessageWhenCreateDirectorIsEmpty() {
        shouldSendErrorMessageWhenCreateUserIsEmpty(DIRECTOR);
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveDirectorWhenDirectorWithGivenUsernameAlreadyExists() {
        shouldSaveUserWhenAllFieldsAreValid(DIRECTOR);
        shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(DIRECTOR);
    }

    @Test
    @DirtiesContext
    public void shouldSaveDirectorWhenAllFieldsAreValid() {
        shouldSaveUserWhenAllFieldsAreValid(DIRECTOR);
    }
}
