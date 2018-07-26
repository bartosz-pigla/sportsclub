package integrationTest.user.customer;

import static org.junit.Assert.assertEquals;
import static web.common.RequestMappings.ADMIN_CONSOLE_CUSTOMER_BY_USERNAME;
import static web.common.RequestMappings.getParameteredRequestMapping;
import static web.common.RequestParameters.USERNAME_PARAMETER;

import java.util.List;
import java.util.UUID;

import api.user.command.ActivateCustomerCommand;
import api.user.command.CreateUserCommand;
import api.user.command.DeleteUserCommand;
import api.user.command.SendActivationLinkCommand;
import commons.ErrorCode;
import integrationTest.user.AbstractUserItTest;
import org.axonframework.commandhandling.gateway.CommandGateway;
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
import query.model.user.UserEntity;
import query.model.user.UserType;
import query.repository.ActivationLinkEntryRepository;
import query.repository.UserEntityRepository;
import web.common.dto.DeleteUserWebCommand;

public final class DeleteCustomerItTest extends AbstractUserItTest {

    @Autowired
    private ActivationLinkEntryRepository activationRepository;
    @Autowired
    private UserEntityRepository userRepository;
    @Autowired
    private CommandGateway commandGateway;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DirtiesContext
    public void shouldDeleteCustomerWhenIsCreatedAndActivated() {
        CreateUserCommand createCustomerCommand = createCustomer();
        activateCustomer(createCustomerCommand);

        signIn("superuser", "password");
        ResponseEntity<DeleteUserWebCommand> deleteCustomerResponse = restTemplate.exchange(
                getParameteredRequestMapping(ADMIN_CONSOLE_CUSTOMER_BY_USERNAME, USERNAME_PARAMETER, createCustomerCommand.getUsername()),
                HttpMethod.DELETE,
                null,
                DeleteUserWebCommand.class);

        assertEquals(deleteCustomerResponse.getStatusCode(), HttpStatus.OK);

        DeleteUserWebCommand responseBody = deleteCustomerResponse.getBody();
        assertEquals(createCustomerCommand.getEmail().getEmail(), responseBody.getEmail());
        assertEquals(createCustomerCommand.getPhoneNumber().getPhoneNumber(), responseBody.getPhoneNumber());
        assertEquals(createCustomerCommand.getUsername(), responseBody.getUsername());
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteAlreadyDeletedCustomer() {
        CreateUserCommand createCustomerCommand = createCustomer();
        activateCustomer(createCustomerCommand);
        deleteCustomer(userRepository.findByUsername(createCustomerCommand.getUsername()).get().getId());

        signIn("superuser", "password");
        ResponseEntity<List> deleteCustomerResponse = restTemplate.exchange(
                getParameteredRequestMapping(ADMIN_CONSOLE_CUSTOMER_BY_USERNAME, USERNAME_PARAMETER, createCustomerCommand.getUsername()),
                HttpMethod.DELETE,
                null,
                List.class);

        assertEquals(deleteCustomerResponse.getStatusCode(), HttpStatus.CONFLICT);

        List errors = deleteCustomerResponse.getBody();
        assertField("username", ErrorCode.ALREADY_DELETED.getCode(), errors);
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenCustomerNotExists() {
        signIn("superuser", "password");
        ResponseEntity<List> deleteCustomerResponse = restTemplate.exchange(
                getParameteredRequestMapping(ADMIN_CONSOLE_CUSTOMER_BY_USERNAME, USERNAME_PARAMETER, "notExistingCustomerUsername"),
                HttpMethod.DELETE,
                null,
                List.class);

        assertEquals(deleteCustomerResponse.getStatusCode(), HttpStatus.CONFLICT);

        List errors = deleteCustomerResponse.getBody();
        assertField("username", ErrorCode.NOT_EXISTS.getCode(), errors);
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
        UserEntity customer = userRepository.findByUsername(command.getUsername()).get();

        SendActivationLinkCommand sendActivationLinkCommand = SendActivationLinkCommand.builder()
                .customerId(customer.getId()).build();
        commandGateway.sendAndWait(sendActivationLinkCommand);

        ActivationLinkEntry activationLink = activationRepository.findByCustomer(customer);
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
