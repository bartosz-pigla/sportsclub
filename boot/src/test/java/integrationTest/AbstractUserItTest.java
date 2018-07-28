package integrationTest;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Optional;

import commons.ErrorCode;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import query.model.user.UserEntity;
import query.repository.UserEntityRepository;
import web.common.dto.CreateUserWebCommand;

public abstract class AbstractUserItTest extends IntegrationTest {

    @Autowired
    protected UserEntityRepository userRepository;
    protected CreateUserWebCommand createUserWebCommand;

    @Before
    public void initCustomerCommand() {
        createUserWebCommand = CreateUserWebCommand.builder()
                .username("username")
                .password("password")
                .email("bartosz.pigla@o2.pl")
                .phoneNumber("+48664330504").build();
    }

    protected void shouldSendErrorMessageWhenCreateUserIsEmpty(String userRequestMapping) {
        ResponseEntity<List> responseEntity = restTemplate.postForEntity(userRequestMapping, new CreateUserWebCommand(), List.class);
        List responseBody = responseEntity.getBody();

        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertField("username", ErrorCode.EMPTY.getCode(), responseBody);
        assertField("password", ErrorCode.EMPTY.getCode(), responseBody);
        assertField("email", ErrorCode.EMPTY.getCode(), responseBody);
        assertField("phoneNumber", ErrorCode.EMPTY.getCode(), responseBody);
    }

    protected void shouldSaveUserWhenAllFieldsAreValid(String userRequestMapping) {
        assertFalse(userRepository.existsByUsername(createUserWebCommand.getUsername()));

        ResponseEntity<CreateUserWebCommand> responseEntity = restTemplate.postForEntity(
                userRequestMapping, createUserWebCommand, CreateUserWebCommand.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(responseEntity.getBody(), createUserWebCommand);

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(createUserWebCommand.getUsername());
        assertTrue(userEntityOptional.isPresent());
        assertFalse(userEntityOptional.get().isActivated());
    }

    protected void shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(String userRequestMapping) {
        ResponseEntity<List> responseEntity = restTemplate.postForEntity(
                userRequestMapping, createUserWebCommand, List.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CONFLICT);

        List responseBody = responseEntity.getBody();
        assertField("username", "alreadyExists", responseBody);
    }

}
