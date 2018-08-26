package integrationTest.customerApi.booking;

import static org.junit.Assert.assertEquals;
import static query.model.booking.repository.BookingQueryExpressions.userIdMatches;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_SUBMIT;

import java.time.LocalDate;
import java.util.UUID;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import api.booking.command.CreateBookingCommand;
import integrationTest.AbstractBookingItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.booking.BookingState;

public final class SubmitBookingItTest extends AbstractBookingItTest {

    @Test
    @DirtiesContext
    public void shouldSubmit() {
        signIn("customer", "password");
        commandGateway.sendAndWait(new CreateBookingCommand(customerId));
        UUID bookingId = bookingRepository.findOne(userIdMatches(customerId)).get().getId();

        commandGateway.sendAndWait(AddBookingDetailCommand.builder()
                .bookingId(bookingId)
                .customerId(customerId)
                .openingTimeId(openingTimeId)
                .sportObjectPositionId(sportObjectPositionId)
                .date(LocalDate.now())
                .build());

        ResponseEntity<String> cancelBookingResponse = patch(
                CUSTOMER_API_BOOKING_SUBMIT,
                null,
                String.class,
                bookingId);

        assertEquals(cancelBookingResponse.getStatusCode(), HttpStatus.NO_CONTENT);
        assertEquals(bookingRepository.findOne(userIdMatches(customerId)).get().getState(), BookingState.SUBMITTED);
    }
}
