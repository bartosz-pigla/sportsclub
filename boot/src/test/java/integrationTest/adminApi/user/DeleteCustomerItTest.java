package integrationTest.adminApi.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.DIRECTOR_API_CUSTOMER_BY_ID;

import java.util.UUID;

import api.user.command.DeleteUserCommand;
import integrationTest.AbstractUserItTest;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.user.UserType;
import query.model.user.repository.UserQueryExpressions;

public final class DeleteCustomerItTest extends AbstractUserItTest {

    @Test
    @DirtiesContext
    public void shouldDeleteCustomerWhenIsCreatedAndActivated() {
        UUID customerId = createUser(UserType.CUSTOMER);
        activateCustomer(customerId);

        signIn("superuser", "password");
        ResponseEntity<String> deleteCustomerResponse = restTemplate.exchange(
                DIRECTOR_API_CUSTOMER_BY_ID,
                HttpMethod.DELETE,
                null,
                String.class,
                customerId.toString());

        assertEquals(deleteCustomerResponse.getStatusCode(), HttpStatus.NO_CONTENT);
        assertFalse(userRepository.exists(UserQueryExpressions.idMatches(customerId)));
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteAlreadyDeletedCustomer() {
        UUID customerId = createUser(UserType.CUSTOMER);
        activateCustomer(customerId);
        commandGateway.sendAndWait(new DeleteUserCommand(customerId));
        signIn("superuser", "password");

        ResponseEntity<String> deleteCustomerResponse = restTemplate.exchange(
                DIRECTOR_API_CUSTOMER_BY_ID,
                HttpMethod.DELETE,
                null,
                String.class,
                customerId);

        assertEquals(deleteCustomerResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenCustomerNotExists() {
        signIn("superuser", "password");

        ResponseEntity<String> deleteCustomerResponse = restTemplate.exchange(
                DIRECTOR_API_CUSTOMER_BY_ID,
                HttpMethod.DELETE,
                null,
                String.class,
                "notExistingCustomerId");

        assertEquals(deleteCustomerResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
