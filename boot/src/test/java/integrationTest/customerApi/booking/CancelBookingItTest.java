package integrationTest.customerApi.booking;

import static org.junit.Assert.assertEquals;
import static query.model.booking.repository.BookingQueryExpressions.userIdMatches;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_CANCEL;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import api.booking.command.ConfirmBookingCommand;
import api.booking.command.CreateBookingCommand;
import api.booking.command.FinishBookingCommand;
import api.booking.command.SubmitBookingCommand;
import commons.ErrorCode;
import integrationTest.AbstractBookingItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.booking.BookingState;

public final class CancelBookingItTest extends AbstractBookingItTest {

    @Test
    @DirtiesContext
    public void shouldCancel() {
        signIn("customer", "password");
        commandGateway.sendAndWait(new CreateBookingCommand(customerId));
        UUID bookingId = bookingRepository.findOne(userIdMatches(customerId)).get().getId();

        ResponseEntity<String> cancelBookingResponse = patch(
                CUSTOMER_API_BOOKING_CANCEL,
                null,
                String.class,
                bookingId);

        assertEquals(cancelBookingResponse.getStatusCode(), HttpStatus.NO_CONTENT);
        assertEquals(bookingRepository.findOne(userIdMatches(customerId)).get().getState(), BookingState.CANCELED);
    }

    @Test
    @DirtiesContext
    public void shouldNotCancelWhenIsFinished() {
        signIn("customer", "password");
        commandGateway.sendAndWait(new CreateBookingCommand(customerId));
        UUID bookingId = bookingRepository.findOne(userIdMatches(customerId)).get().getId();

        commandGateway.sendAndWait(AddBookingDetailCommand.builder()
                .bookingId(bookingId)
                .customerId(customerId)
                .sportObjectPositionId(sportObjectPositionId)
                .openingTimeId(openingTimeId)
                .date(LocalDate.now().plusDays(1))
                .build());

        commandGateway.sendAndWait(new SubmitBookingCommand(bookingId, customerId));
        commandGateway.sendAndWait(new ConfirmBookingCommand(bookingId));
        commandGateway.sendAndWait(new FinishBookingCommand(bookingId));

        ResponseEntity<List> cancelBookingResponse = patch(
                CUSTOMER_API_BOOKING_CANCEL,
                null,
                List.class,
                bookingId);

        assertEquals(cancelBookingResponse.getStatusCode(), HttpStatus.CONFLICT);
        List errors = cancelBookingResponse.getBody();
        assertField("state", ErrorCode.INVALID.getCode(), errors);
        assertEquals(bookingRepository.findOne(userIdMatches(customerId)).get().getState(), BookingState.FINISHED);
    }
}
