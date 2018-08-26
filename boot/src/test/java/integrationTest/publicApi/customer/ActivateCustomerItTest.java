package integrationTest.publicApi.customer;

import static org.junit.Assert.assertEquals;
import static query.model.user.repository.ActivationLinkQueryExpressions.customerNameMatches;
import static web.common.RequestMappings.PUBLIC_API_CUSTOMER_ACTIVATE;
import static web.common.RequestMappings.PUBLIC_API_SIGN_UP;

import java.util.UUID;

import integrationTest.AbstractUserItTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.user.ActivationLinkEntry;
import query.model.user.repository.ActivationLinkEntryRepository;
import query.model.user.repository.UserQueryExpressions;
import web.common.user.dto.UserDto;
import web.publicApi.signUp.dto.ActivateCustomerWebCommand;

public final class ActivateCustomerItTest extends AbstractUserItTest {

    @Autowired
    private ActivationLinkEntryRepository activationLinkRepository;

    @Test
    @DirtiesContext
    public void shouldActivateCustomerWhenActivationLinkIsValid() {
        ResponseEntity<UserDto> createCustomerResponse = restTemplate.postForEntity(
                PUBLIC_API_SIGN_UP, createUserWebCommand, UserDto.class);

        assertEquals(createCustomerResponse.getStatusCode(), HttpStatus.OK);

        UUID customerId = userRepository.findOne(
                UserQueryExpressions.usernameMatches(createUserWebCommand.getUsername())).get().getId();

        ActivationLinkEntry activationLink = activationLinkRepository.findOne(
                customerNameMatches(createUserWebCommand.getUsername())).get();

        ResponseEntity<String> activateResponse = patch(
                PUBLIC_API_CUSTOMER_ACTIVATE,
                new ActivateCustomerWebCommand(activationLink.getId().toString()),
                String.class,
                customerId.toString());

        assertEquals(activateResponse.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @DirtiesContext
    public void shouldNotActivateCustomerWhenActivationLinkIsInvalid() {
        ResponseEntity<UserDto> createCustomerResponse = restTemplate.postForEntity(
                PUBLIC_API_SIGN_UP, createUserWebCommand, UserDto.class);

        assertEquals(createCustomerResponse.getStatusCode(), HttpStatus.OK);

        UUID customerId = userRepository.findOne(
                UserQueryExpressions.usernameMatches(createUserWebCommand.getUsername())).get().getId();

        ResponseEntity activateResponse = patch(
                PUBLIC_API_CUSTOMER_ACTIVATE,
                new ActivateCustomerWebCommand(UUID.randomUUID().toString()),
                null,
                customerId.toString());

        assertEquals(activateResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
