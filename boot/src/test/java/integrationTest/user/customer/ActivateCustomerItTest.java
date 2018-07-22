package integrationTest.user.customer;

import static org.junit.Assert.assertEquals;
import static web.common.RequestMappings.CUSTOMER_ACTIVATION;
import static web.common.RequestMappings.SIGN_UP;

import java.util.UUID;

import integrationTest.user.AbstractUserItTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.user.ActivationLinkEntry;
import query.model.user.UserEntity;
import query.repository.ActivationLinkEntryRepository;
import web.signUp.dto.ActivateCustomerWebCommand;
import web.signUp.dto.CreateUserWebCommand;

public final class ActivateCustomerItTest extends AbstractUserItTest {

    @Autowired
    private ActivationLinkEntryRepository activationRepository;

    @Test
    @DirtiesContext
    public void shouldActivateCustomerWhenActivationLinkIsValid() {
        ResponseEntity<CreateUserWebCommand> createCustomerResponse = restTemplate.postForEntity(
                SIGN_UP, createUserWebCommand, CreateUserWebCommand.class);
        assertEquals(createCustomerResponse.getStatusCode(), HttpStatus.OK);

        UserEntity customer = userRepository.findByUsername(createUserWebCommand.getUsername()).get();
        ActivationLinkEntry activationLink = activationRepository.findByUser(customer);

        ActivateCustomerWebCommand activateCustomerCommand = ActivateCustomerWebCommand.builder()
                .activationKey(activationLink.getId().toString()).build();
        ResponseEntity<String> activateResponse = restTemplate.postForEntity(
                CUSTOMER_ACTIVATION, activateCustomerCommand, String.class);
        assertEquals(activateResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void shouldNotActivateCustomerWhenActivationLinkIsInvalid() {
        ResponseEntity<CreateUserWebCommand> createCustomerResponse = restTemplate.postForEntity(
                SIGN_UP, createUserWebCommand, CreateUserWebCommand.class);
        assertEquals(createCustomerResponse.getStatusCode(), HttpStatus.OK);

        ActivateCustomerWebCommand activateCustomerCommand = ActivateCustomerWebCommand.builder()
                .activationKey(UUID.randomUUID().toString()).build();
        ResponseEntity activateResponse = restTemplate.postForEntity(
                CUSTOMER_ACTIVATION, activateCustomerCommand, null);
        assertEquals(activateResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
