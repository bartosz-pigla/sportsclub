package integrationTest;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static query.model.user.repository.ActivationLinkQueryExpressions.customerIdMatches;
import static query.model.user.repository.UserQueryExpressions.usernameMatches;

import java.util.List;
import java.util.UUID;

import api.user.command.ActivateCustomerCommand;
import api.user.command.CreateUserCommand;
import api.user.command.SendActivationLinkCommand;
import commons.ErrorCode;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserType;
import query.model.user.repository.ActivationLinkEntryRepository;
import query.model.user.repository.UserEntityRepository;
import query.model.user.repository.UserQueryExpressions;
import web.user.dto.CreateUserWebCommand;
import web.user.dto.UserDto;

public abstract class AbstractUserItTest extends IntegrationTest {

    @Autowired
    protected UserEntityRepository userRepository;
    @Autowired
    protected ActivationLinkEntryRepository activationLinkRepository;
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
        ResponseEntity<List> responseEntity = restTemplate.postForEntity(
                userRequestMapping,
                new CreateUserWebCommand(),
                List.class);

        List responseBody = responseEntity.getBody();

        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertField("username", ErrorCode.EMPTY.getCode(), responseBody);
        assertField("password", ErrorCode.EMPTY.getCode(), responseBody);
        assertField("email", ErrorCode.EMPTY.getCode(), responseBody);
        assertField("phoneNumber", ErrorCode.EMPTY.getCode(), responseBody);
    }

    protected void shouldSaveUserWhenAllFieldsAreValid(String userRequestMapping) {
        String username = createUserWebCommand.getUsername();
        assertFalse(userRepository.exists(usernameMatches(username)));

        ResponseEntity<UserDto> responseEntity = restTemplate.postForEntity(
                userRequestMapping, createUserWebCommand, UserDto.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        UserDto responseBody = responseEntity.getBody();
        assertEquals(responseBody.getUsername(), username);
        assertTrue(userRepository.exists(usernameMatches(username)));
    }

    protected void shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(String userRequestMapping) {
        ResponseEntity<List> responseEntity = restTemplate.postForEntity(
                userRequestMapping,
                createUserWebCommand,
                List.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CONFLICT);

        List responseBody = responseEntity.getBody();
        assertField("username", "alreadyExists", responseBody);
    }

    protected UUID createUser(UserType userType) {
        String username = "username";

        CreateUserCommand createCommand = CreateUserCommand.builder()
                .username(username)
                .password("password")
                .userType(userType)
                .email(new Email("bartek21@wp.pl"))
                .phoneNumber(new PhoneNumber("+48664220504"))
                .build();

        commandGateway.sendAndWait(createCommand);

        return userRepository.findOne(
                UserQueryExpressions.usernameMatches(username)).get().getId();
    }

    protected UUID activateCustomer(UUID customerId) {
        commandGateway.sendAndWait(new SendActivationLinkCommand(customerId));
        UUID activationKey = activationLinkRepository.findOne(customerIdMatches(customerId)).get().getId();
        commandGateway.sendAndWait(new ActivateCustomerCommand(customerId, activationKey));
        return activationKey;
    }
}
