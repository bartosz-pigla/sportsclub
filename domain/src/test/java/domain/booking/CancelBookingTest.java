package domain.booking;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import api.booking.command.CancelBookingCommand;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingRejectedEvent;
import domain.booking.exception.AlreadyCanceledException;
import domain.booking.exception.AlreadyRejectedException;
import org.junit.Test;

public final class CancelBookingTest extends AbstractBookingTest {

    private CancelBookingCommand cancelCommand = CancelBookingCommand.builder()
            .bookingId(bookingId)
            .build();

    @Test
    public void shouldNotCancelWhenIsAlreadyCanceled() {
        testFixture.given(bookingCreatedEvent, new BookingCanceledEvent(bookingId))
                .when(cancelCommand)
                .expectNoEvents()
                .expectException(AlreadyCanceledException.class);
    }

    @Test
    public void shouldNotCancelWhenIsAlreadyRejected() {
        testFixture.given(bookingCreatedEvent, new BookingRejectedEvent(bookingId))
                .when(cancelCommand)
                .expectNoEvents()
                .expectException(AlreadyRejectedException.class);
    }

    @Test
    public void shouldCancel() {
        testFixture.given(bookingCreatedEvent)
                .when(cancelCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingCanceledEvent event = (BookingCanceledEvent) p.getPayload();
                    return event.getBookingId().equals(bookingId);
                }), andNoMore()));
    }
}
