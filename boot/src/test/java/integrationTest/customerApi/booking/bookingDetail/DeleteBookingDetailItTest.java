package integrationTest.customerApi.booking.bookingDetail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static query.model.booking.repository.BookingDetailQueryExpressions.bookingDetailMatches;
import static query.model.booking.repository.BookingQueryExpressions.userIdMatches;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_DETAIL_BY_ID;

import java.time.LocalDate;
import java.util.UUID;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import api.booking.command.CreateBookingCommand;
import integrationTest.AbstractBookingItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.booking.repository.BookingDetailQueryExpressions;
import query.model.embeddable.OpeningTimeRange;
import query.model.sportobject.repository.OpeningTimeQueryExpressions;

public final class DeleteBookingDetailItTest extends AbstractBookingItTest {

    @Test
    @DirtiesContext
    public void shouldDelete() {
        signIn("customer", "password");
        commandGateway.sendAndWait(new CreateBookingCommand(customerId));
        UUID bookingId = bookingRepository.findOne(userIdMatches(customerId)).get().getId();
        LocalDate date = LocalDate.now();
        commandGateway.sendAndWait(AddBookingDetailCommand.builder()
                .bookingId(bookingId)
                .customerId(customerId)
                .openingTimeId(openingTimeId)
                .sportObjectPositionId(sportObjectPositionId)
                .date(date)
                .build());

        OpeningTimeRange timeRange = openingTimeRepository.findOne(OpeningTimeQueryExpressions.idMatches(openingTimeId)).get().getTimeRange();
        UUID bookingDetailId = bookingDetailRepository.findOne(bookingDetailMatches(
                sportObjectPositionId,
                date,
                timeRange)).get().getId();

        ResponseEntity<String> deleteDetailResponse = delete(CUSTOMER_API_BOOKING_DETAIL_BY_ID, String.class, bookingId, bookingDetailId);

        assertEquals(deleteDetailResponse.getStatusCode(), HttpStatus.NO_CONTENT);
        assertFalse(bookingDetailRepository.findOne(BookingDetailQueryExpressions.idMatches(bookingDetailId)).isPresent());
    }
}
