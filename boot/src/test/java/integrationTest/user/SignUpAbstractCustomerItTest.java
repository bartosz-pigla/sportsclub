package integrationTest.user;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.SIGN_UP;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.user.UserEntity;
import web.signUp.dto.CreateCustomerWebCommand;

public final class SignUpAbstractCustomerItTest extends AbstractCustomerItTest {

    @Test
    public void shouldSendErrorMessageWhenCreateCustomerIsEmpty() {
        ResponseEntity<List> responseEntity = restTemplate.postForEntity(SIGN_UP, new CreateCustomerWebCommand(), List.class);
        List responseBody = responseEntity.getBody();

        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertField("username", "username.empty", responseBody);
        assertField("password", "password.empty", responseBody);
        assertField("email", "email.empty", responseBody);
        assertField("phoneNumber", "phoneNumber.empty", responseBody);
        assertTrue(true);
    }

    @Test
    @DirtiesContext
    public void shouldSaveCustomerWhenAllFieldsAreValid() {
        assertFalse(userRepository.existsByUsername(createCustomerCommand.getUsername()));

        ResponseEntity<CreateCustomerWebCommand> responseEntity = restTemplate.postForEntity(
                SIGN_UP, createCustomerCommand, CreateCustomerWebCommand.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(responseEntity.getBody(), createCustomerCommand);

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(createCustomerCommand.getUsername());
        assertTrue(userEntityOptional.isPresent());
        assertFalse(userEntityOptional.get().isActivated());
        assertTrue(true);
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveCustomerWhenUserWithGivenUsernameAlreadyExists() {
        shouldSaveCustomerWhenAllFieldsAreValid();
        ResponseEntity<List> responseEntity = restTemplate.postForEntity(
                SIGN_UP, createCustomerCommand, List.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CONFLICT);

        List responseBody = responseEntity.getBody();
        assertField("username", "alreadyExists", responseBody);
        assertTrue(true);
    }
}
