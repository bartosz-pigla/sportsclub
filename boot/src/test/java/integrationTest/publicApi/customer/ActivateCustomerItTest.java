package integrationTest.publicApi.customer;

import static org.junit.Assert.assertEquals;
import static query.model.user.repository.ActivationLinkQueryExpressions.customerNameMatches;
import static web.common.RequestMappings.CUSTOMER_ACTIVATION;
import static web.common.RequestMappings.SIGN_UP;

import java.util.UUID;

import integrationTest.AbstractUserItTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.user.ActivationLinkEntry;
import query.model.user.repository.ActivationLinkEntryRepository;
import web.common.user.dto.CreateUserWebCommand;
import web.publicApi.signUp.dto.ActivateCustomerWebCommand;

public final class ActivateCustomerItTest extends AbstractUserItTest {

    @Autowired
    private ActivationLinkEntryRepository activationLinkRepository;

    @Test
    @DirtiesContext
    public void shouldActivateCustomerWhenActivationLinkIsValid() {
        ResponseEntity<CreateUserWebCommand> createCustomerResponse = restTemplate.postForEntity(
                SIGN_UP, createUserWebCommand, CreateUserWebCommand.class);
        assertEquals(createCustomerResponse.getStatusCode(), HttpStatus.OK);

        ActivationLinkEntry activationLink = activationLinkRepository.findOne(customerNameMatches(createUserWebCommand.getUsername())).get();

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
