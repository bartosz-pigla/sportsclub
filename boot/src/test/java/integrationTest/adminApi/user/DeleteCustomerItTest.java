package integrationTest.adminApi.user;

import static org.junit.Assert.assertEquals;
import static query.model.user.repository.ActivationLinkQueryExpressions.customerIdMatches;
import static query.model.user.repository.UserQueryExpressions.usernameMatches;
import static web.common.RequestMappings.ADMIN_API_CUSTOMER_BY_USERNAME;

import java.util.List;
import java.util.UUID;

import api.user.command.ActivateCustomerCommand;
import api.user.command.CreateUserCommand;
import api.user.command.DeleteUserCommand;
import api.user.command.SendActivationLinkCommand;
import integrationTest.AbstractUserItTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.ActivationLinkEntry;
import query.model.user.UserType;
import query.model.user.dto.UserDto;
import query.model.user.repository.ActivationLinkEntryRepository;
import query.model.user.repository.UserEntityRepository;

public final class DeleteCustomerItTest extends AbstractUserItTest {

    @Autowired
    private ActivationLinkEntryRepository activationLinkRepository;
    @Autowired
    private UserEntityRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DirtiesContext
    public void shouldDeleteCustomerWhenIsCreatedAndActivated() {
        CreateUserCommand createCustomerCommand = createCustomer();
        activateCustomer(createCustomerCommand);

        signIn("superuser", "password");
        ResponseEntity<UserDto> deleteCustomerResponse = restTemplate.exchange(
                ADMIN_API_CUSTOMER_BY_USERNAME,
                HttpMethod.DELETE,
                null,
                UserDto.class,
                createCustomerCommand.getUsername());

        assertEquals(deleteCustomerResponse.getStatusCode(), HttpStatus.OK);

        UserDto responseBody = deleteCustomerResponse.getBody();
        assertEquals(createCustomerCommand.getEmail().getEmail(), responseBody.getEmail());
        assertEquals(createCustomerCommand.getPhoneNumber().getPhoneNumber(), responseBody.getPhoneNumber());
        assertEquals(createCustomerCommand.getUsername(), responseBody.getUsername());
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteAlreadyDeletedCustomer() {
        CreateUserCommand createCustomerCommand = createCustomer();
        activateCustomer(createCustomerCommand);
        deleteCustomer(userRepository.findOne(usernameMatches(createCustomerCommand.getUsername())).get().getId());

        signIn("superuser", "password");
        ResponseEntity<List> deleteCustomerResponse = restTemplate.exchange(
                ADMIN_API_CUSTOMER_BY_USERNAME,
                HttpMethod.DELETE,
                null,
                List.class,
                createCustomerCommand.getUsername());

        assertEquals(deleteCustomerResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenCustomerNotExists() {
        signIn("superuser", "password");
        ResponseEntity<List> deleteCustomerResponse = restTemplate.exchange(
                ADMIN_API_CUSTOMER_BY_USERNAME,
                HttpMethod.DELETE,
                null,
                List.class,
                "notExistingCustomerUsername");

        assertEquals(deleteCustomerResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    private CreateUserCommand createCustomer() {
        CreateUserCommand createCustomerCommand = CreateUserCommand.builder()
                .phoneNumber(new PhoneNumber("+48664220607"))
                .email(new Email("bartek217a@wp.pl"))
                .userType(UserType.CUSTOMER)
                .password(passwordEncoder.encode("password1"))
                .username("test-customer1").build();
        commandGateway.sendAndWait(createCustomerCommand);
        return createCustomerCommand;
    }

    private ActivateCustomerCommand activateCustomer(CreateUserCommand command) {
        UUID customerId = userRepository.findOne(usernameMatches(command.getUsername())).get().getId();

        SendActivationLinkCommand sendActivationLinkCommand = SendActivationLinkCommand.builder()
                .customerId(customerId).build();
        commandGateway.sendAndWait(sendActivationLinkCommand);

        ActivationLinkEntry activationLink = activationLinkRepository.findOne(customerIdMatches(customerId)).get();

        ActivateCustomerCommand activateCustomerCommand = ActivateCustomerCommand.builder()
                .activationKey(activationLink.getId())
                .customerId(activationLink.getCustomer().getId()).build();
        commandGateway.sendAndWait(activateCustomerCommand);
        return activateCustomerCommand;
    }

    private DeleteUserCommand deleteCustomer(UUID customerId) {
        DeleteUserCommand deleteCustomerCommand = DeleteUserCommand.builder()
                .userId(customerId).build();
        commandGateway.sendAndWait(deleteCustomerCommand);
        return deleteCustomerCommand;
    }
}
