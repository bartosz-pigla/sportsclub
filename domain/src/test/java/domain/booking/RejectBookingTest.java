package domain.booking;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import api.booking.command.RejectBookingCommand;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingFinishedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmittedEvent;
import domain.booking.exception.IllegalBookingStateException;
import org.junit.Test;

public final class RejectBookingTest extends AbstractBookingTest {

    private RejectBookingCommand rejectCommand = RejectBookingCommand.builder()
            .bookingId(bookingId)
            .customerId(customerId)
            .build();

    @Test
    public void shouldNotRejectWhenIsAlreadyRejected() {
        testFixture.given(bookingCreatedEvent, new BookingSubmittedEvent(bookingId), new BookingRejectedEvent(bookingId))
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotRejectedWhenIsNotSubmitted() {
        testFixture.given(bookingCreatedEvent)
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotRejectWhenIsAlreadyCanceled() {
        testFixture.given(bookingCreatedEvent, new BookingCanceledEvent(bookingId))
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotRejectWhenIsAlreadyConfirmed() {
        testFixture.given(bookingCreatedEvent, new BookingSubmittedEvent(bookingId), new BookingConfirmedEvent(bookingId))
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotRejectWhenIsAlreadyFinished() {
        testFixture.given(
                bookingCreatedEvent,
                new BookingSubmittedEvent(bookingId),
                new BookingConfirmedEvent(bookingId),
                new BookingFinishedEvent(bookingId))
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldReject() {
        testFixture.given(bookingCreatedEvent, new BookingSubmittedEvent(bookingId))
                .when(rejectCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingRejectedEvent event = (BookingRejectedEvent) p.getPayload();
                    return event.getBookingId().equals(bookingId);
                }), andNoMore()));
    }
}
