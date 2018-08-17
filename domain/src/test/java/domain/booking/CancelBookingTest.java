package domain.booking;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import api.booking.command.CancelBookingCommand;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmittedEvent;
import domain.booking.exception.IllegalBookingStateException;
import org.junit.Test;

public final class CancelBookingTest extends AbstractBookingTest {

    private CancelBookingCommand cancelCommand = CancelBookingCommand.builder()
            .bookingId(bookingId)
            .customerId(customerId)
            .build();

    @Test
    public void shouldNotCancelWhenIsAlreadyCanceled() {
        testFixture.given(bookingCreatedEvent, new BookingCanceledEvent(bookingId))
                .when(cancelCommand)
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotCancelWhenIsAlreadyRejected() {
        testFixture.given(bookingCreatedEvent, new BookingRejectedEvent(bookingId))
                .when(cancelCommand)
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldCancelWhenIsCreated() {
        testFixture.given(bookingCreatedEvent)
                .when(cancelCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingCanceledEvent event = (BookingCanceledEvent) p.getPayload();
                    return event.getBookingId().equals(bookingId);
                }), andNoMore()));
    }

    @Test
    public void shouldCancelWhenIsSubmitted() {
        testFixture.given(bookingCreatedEvent, new BookingSubmittedEvent(bookingId))
                .when(cancelCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingCanceledEvent event = (BookingCanceledEvent) p.getPayload();
                    return event.getBookingId().equals(bookingId);
                }), andNoMore()));
    }

    @Test
    public void shouldCancelWhenIsConfirmed() {
        testFixture.given(bookingCreatedEvent, new BookingSubmittedEvent(bookingId), new BookingConfirmedEvent(bookingId))
                .when(cancelCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingCanceledEvent event = (BookingCanceledEvent) p.getPayload();
                    return event.getBookingId().equals(bookingId);
                }), andNoMore()));
    }
}
