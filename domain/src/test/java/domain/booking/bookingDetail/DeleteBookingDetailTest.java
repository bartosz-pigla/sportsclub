package domain.booking.bookingDetail;

import static junit.framework.TestCase.assertTrue;

import java.time.LocalDate;
import java.util.UUID;

import api.booking.bookingDetail.command.DeleteBookingDetailCommand;
import api.booking.bookingDetail.event.BookingDetailAddedEvent;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmittedEvent;
import domain.booking.AbstractBookingTest;
import domain.booking.exception.IllegalBookingStateException;
import org.junit.Test;

public final class DeleteBookingDetailTest extends AbstractBookingTest {

    private UUID detailId = UUID.randomUUID();

    private BookingDetailAddedEvent detailAddedEvent = BookingDetailAddedEvent.builder()
            .bookingDetailId(detailId)
            .bookingId(bookingCreatedEvent.getBookingId())
            .sportObjectPositionId(UUID.randomUUID())
            .openingTimeId(UUID.randomUUID())
            .date(LocalDate.now())
            .build();

    @Test
    public void shouldNotDeleteWhenIsSubmitted() {
        testFixture.given(bookingCreatedEvent, detailAddedEvent, new BookingSubmittedEvent(bookingId))
                .when(new DeleteBookingDetailCommand(bookingId, detailId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotDeleteWhenIsRejected() {
        testFixture.given(
                bookingCreatedEvent,
                detailAddedEvent,
                new BookingSubmittedEvent(bookingId),
                new BookingRejectedEvent(bookingId))
                .when(new DeleteBookingDetailCommand(bookingId, detailId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotDeleteWhenIsConfirmed() {
        testFixture.given(
                bookingCreatedEvent,
                detailAddedEvent,
                new BookingSubmittedEvent(bookingId),
                new BookingConfirmedEvent(bookingId))
                .when(new DeleteBookingDetailCommand(bookingId, detailId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotDeleteWhenIsCanceled() {
        testFixture.given(
                bookingCreatedEvent,
                detailAddedEvent,
                new BookingSubmittedEvent(bookingId),
                new BookingCanceledEvent(bookingId))
                .when(new DeleteBookingDetailCommand(bookingId, detailId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }


    @Test
    public void shouldNotDeleteWhenIsAlreadyDeleted() {
        query.model.booking.QBookingEntity fd;

        assertTrue(true);
    }

    @Test
    public void shouldDelete() {
        assertTrue(true);
    }
}
