package integrationTest.customerApi.booking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static query.model.booking.repository.BookingQueryExpressions.usernameMatches;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING;

import integrationTest.AbstractBookingItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

public final class CreateBookingItTest extends AbstractBookingItTest {

    @Test
    @DirtiesContext
    public void shouldCreate() {
        signIn("customer", "password");

        ResponseEntity<String> createBookingResponse = restTemplate.postForEntity(CUSTOMER_API_BOOKING, null, String.class);

        assertEquals(createBookingResponse.getStatusCode(), HttpStatus.NO_CONTENT);
        assertTrue(bookingRepository.exists(usernameMatches("customer")));
    }
}
