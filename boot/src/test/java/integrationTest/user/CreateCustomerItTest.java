package integrationTest.user;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.SIGN_UP;

import java.util.List;
import java.util.Optional;

import integrationTest.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.user.UserEntity;
import query.repository.UserEntityRepository;
import web.signUp.CreateCustomerCommand;

public final class CreateCustomerItTest extends IntegrationTest {

    @Autowired
    private UserEntityRepository userEntityRepository;
    private CreateCustomerCommand commandToPost;

    @Before
    public void initCustomerCommand() {
        commandToPost = CreateCustomerCommand.builder()
                .username("username")
                .password("password")
                .email("bartosz.pigla@o2.pl")
                .phoneNumber("+48664330504").build();
    }

    @Test
    public void shouldSendErrorMessageWhenCreateCustomerIsEmpty() {
        ResponseEntity<List> responseEntity = restTemplate.postForEntity(SIGN_UP, new CreateCustomerCommand(), List.class);
        List responseBody = responseEntity.getBody();

        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertField("username", "username.empty", responseBody);
        assertField("password", "password.empty", responseBody);
        assertField("email", "email.empty", responseBody);
        assertField("phoneNumber", "phoneNumber.empty", responseBody);
    }

    @Test
    @DirtiesContext
    public void shouldSaveCustomerWhenAllFieldsAreValid() {
        assertFalse(userEntityRepository.existsByUsername(commandToPost.getUsername()));

        ResponseEntity<CreateCustomerCommand> responseEntity = restTemplate.postForEntity(
                SIGN_UP, commandToPost, CreateCustomerCommand.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(responseEntity.getBody(), commandToPost);

        Optional<UserEntity> userEntityOptional = userEntityRepository.findByUsername(commandToPost.getUsername());
        assertTrue(userEntityOptional.isPresent());
        assertFalse(userEntityOptional.get().isActivated());
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveCustomerWhenUserWithGivenUsernameAlreadyExists() {
        shouldSaveCustomerWhenAllFieldsAreValid();
        ResponseEntity<List> responseEntity = restTemplate.postForEntity(
                SIGN_UP, commandToPost, List.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus. CONFLICT);

        List responseBody = responseEntity.getBody();
        assertField("username", "alreadyExists", responseBody);
        assertField("username", commandToPost.getUsername(), responseBody);
    }
}
