package integrationTest.customerApi.booking;

import static org.junit.Assert.assertEquals;
import static query.model.booking.repository.BookingQueryExpressions.userIdMatches;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_BY_ID;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import api.booking.command.CreateBookingCommand;
import api.booking.command.SubmitBookingCommand;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.booking.BookingState;

public final class ConfirmBookingItTest extends AbstractBookingItTest {

    @Test
    @DirtiesContext
    public void shouldConfirm() {
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

        ResponseEntity<List> confirmBookingResponse = patch(CUSTOMER_API_BOOKING_BY_ID, BookingState.CONFIRMED, List.class, bookingId);

        assertEquals(confirmBookingResponse.getStatusCode(), HttpStatus.NO_CONTENT);
        assertEquals(bookingRepository.findOne(userIdMatches(customerId)).get().getState(), BookingState.CONFIRMED);
    }
}
