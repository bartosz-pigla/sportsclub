package integrationTest.adminApi.booking;

import static org.junit.Assert.assertEquals;
import static query.model.booking.repository.BookingQueryExpressions.userIdMatches;
import static web.common.RequestMappings.RECEPTIONIST_API_BOOKING_REJECT;

import java.time.LocalDate;
import java.util.UUID;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import api.booking.command.CreateBookingCommand;
import api.booking.command.SubmitBookingCommand;
import integrationTest.AbstractBookingItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.booking.BookingState;

public final class RejectBookingItTest extends AbstractBookingItTest {

    @Test
    @DirtiesContext
    public void shouldReject() {
        signIn("superuser", "password");
        commandGateway.sendAndWait(new CreateBookingCommand(customerId));
        UUID bookingId = bookingRepository.findOne(userIdMatches(customerId)).get().getId();

        commandGateway.sendAndWait(AddBookingDetailCommand.builder()
                .bookingId(bookingId)
                .customerId(customerId)
                .openingTimeId(openingTimeId)
                .sportObjectPositionId(sportObjectPositionId)
                .date(LocalDate.now())
                .build());

        commandGateway.sendAndWait(new SubmitBookingCommand(bookingId, customerId));

        ResponseEntity<String> rejectBookingResponse = patch(
                RECEPTIONIST_API_BOOKING_REJECT,
                null,
                String.class,
                bookingId.toString());

        assertEquals(rejectBookingResponse.getStatusCode(), HttpStatus.NO_CONTENT);
        assertEquals(bookingRepository.findOne(userIdMatches(customerId)).get().getState(), BookingState.REJECTED);
    }
}
