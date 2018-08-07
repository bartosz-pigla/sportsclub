package domain.booking;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import api.booking.command.CancelBookingCommand;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmitedEvent;
import domain.booking.exception.AlreadyCancelledException;
import domain.booking.exception.AlreadyConfirmedException;
import domain.booking.exception.AlreadyRejectedException;
import domain.booking.exception.AlreadySubmitedException;
import org.junit.Test;

public final class CancelBookingTest extends AbstractBookingTest {

    private CancelBookingCommand cancelCommand = CancelBookingCommand.builder()
            .bookingId(bookingId)
            .build();

    @Test
    public void shouldNotCancelWhenIsAlreadyCanceled() {
        testFixture.given(createdEvent, new BookingCanceledEvent(bookingId))
                .when(cancelCommand)
                .expectNoEvents()
                .expectException(AlreadyCancelledException.class);
    }

    @Test
    public void shouldNotCancelWhenIsAlreadySubmited() {
        testFixture.given(createdEvent, new BookingSubmitedEvent(bookingId))
                .when(cancelCommand)
                .expectNoEvents()
                .expectException(AlreadySubmitedException.class);
    }

    @Test
    public void shouldNotCancelWhenIsAlreadyConfirmed() {
        testFixture.given(createdEvent, new BookingConfirmedEvent(bookingId))
                .when(cancelCommand)
                .expectNoEvents()
                .expectException(AlreadyConfirmedException.class);
    }

    @Test
    public void shouldNotCancelWhenIsAlreadyRejected() {
        testFixture.given(createdEvent, new BookingRejectedEvent(bookingId))
                .when(cancelCommand)
                .expectNoEvents()
                .expectException(AlreadyRejectedException.class);
    }

    @Test
    public void shouldCancel() {
        testFixture.given(createdEvent)
                .when(cancelCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingCanceledEvent event = (BookingCanceledEvent) p.getPayload();
                    return event.getBookingId().equals(bookingId);
                }), andNoMore()));
    }
}
