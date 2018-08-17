package domain.booking.bookingDetail;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.time.LocalDate;
import java.util.UUID;

import api.booking.bookingDetail.command.DeleteBookingDetailCommand;
import api.booking.bookingDetail.event.BookingDetailAddedEvent;
import api.booking.bookingDetail.event.BookingDetailDeletedEvent;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingFinishedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmittedEvent;
import domain.booking.AbstractBookingTest;
import domain.booking.exception.IllegalBookingStateException;
import domain.common.exception.NotExistsException;
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
                .when(new DeleteBookingDetailCommand(bookingId, customerId, detailId))
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
                .when(new DeleteBookingDetailCommand(bookingId, customerId, detailId))
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
                .when(new DeleteBookingDetailCommand(bookingId, customerId, detailId))
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
                .when(new DeleteBookingDetailCommand(bookingId, customerId, detailId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotDeleteWhenIsFinished() {
        testFixture.given(
                bookingCreatedEvent,
                detailAddedEvent,
                new BookingSubmittedEvent(bookingId),
                new BookingConfirmedEvent(bookingId),
                new BookingFinishedEvent(bookingId))
                .when(new DeleteBookingDetailCommand(bookingId, customerId, detailId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotDeleteWhenNotExists() {
        testFixture.given(bookingCreatedEvent)
                .when(new DeleteBookingDetailCommand(bookingId, customerId, detailId))
                .expectNoEvents()
                .expectException(NotExistsException.class);
    }

    @Test
    public void shouldDelete() {
        testFixture.given(bookingCreatedEvent, detailAddedEvent)
                .when(new DeleteBookingDetailCommand(bookingId, customerId, detailId))
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingDetailDeletedEvent event = (BookingDetailDeletedEvent) p.getPayload();
                    return event.getBookingDetailId().equals(detailId);
                }), andNoMore()));
    }
}
