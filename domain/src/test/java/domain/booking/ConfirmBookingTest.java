package domain.booking;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.time.LocalDate;
import java.util.UUID;

import api.booking.bookingDetail.event.BookingDetailAddedEvent;
import api.booking.command.ConfirmBookingCommand;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingFinishedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmittedEvent;
import domain.booking.exception.IllegalBookingStateException;
import org.junit.Test;

public final class ConfirmBookingTest extends AbstractBookingTest {

    @Test
    public void shouldNotConfirmWhenIsAlreadyConfirmed() {
        testFixture.given(bookingCreatedEvent, new BookingSubmittedEvent(bookingId), new BookingConfirmedEvent(bookingId))
                .when(new ConfirmBookingCommand(bookingId, customerId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotConfirmWhenIsAlreadyCanceled() {
        testFixture.given(bookingCreatedEvent, new BookingCanceledEvent(bookingId))
                .when(new ConfirmBookingCommand(bookingId, customerId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotConfirmWhenIsAlreadyRejected() {
        testFixture.given(bookingCreatedEvent, new BookingSubmittedEvent(bookingId), new BookingRejectedEvent(bookingId))
                .when(new ConfirmBookingCommand(bookingId, customerId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotConfirmWhenIsAlreadyFinished() {
        testFixture.given(
                bookingCreatedEvent,
                new BookingSubmittedEvent(bookingId),
                new BookingConfirmedEvent(bookingId),
                new BookingFinishedEvent(bookingId))
                .when(new ConfirmBookingCommand(bookingId, customerId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotConfirmWhenIsNotSubmitted() {
        testFixture.given(bookingCreatedEvent)
                .when(new ConfirmBookingCommand(bookingId, customerId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldConfirm() {
        BookingDetailAddedEvent detailAddedEvent = BookingDetailAddedEvent.builder()
                .bookingDetailId(UUID.randomUUID())
                .openingTimeId(UUID.randomUUID())
                .sportObjectPositionId(UUID.randomUUID())
                .date(LocalDate.now())
                .build();

        testFixture.given(bookingCreatedEvent, detailAddedEvent, new BookingSubmittedEvent(bookingId))
                .when(new ConfirmBookingCommand(bookingId, customerId))
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingConfirmedEvent event = (BookingConfirmedEvent) p.getPayload();
                    return event.getBookingId().equals(bookingId);
                }), andNoMore()));
    }
}
