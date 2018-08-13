package domain.booking;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import api.booking.command.RejectBookingCommand;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmitedEvent;
import domain.booking.exception.AlreadyCanceledException;
import domain.booking.exception.AlreadyConfirmedException;
import domain.booking.exception.AlreadyRejectedException;
import domain.booking.exception.NotSubmitedException;
import org.junit.Test;

public final class RejectBookingTest extends AbstractBookingTest {

    private RejectBookingCommand rejectCommand = RejectBookingCommand.builder()
            .bookingId(bookingId)
            .build();

    @Test
    public void shouldNotRejectWhenIsAlreadyRejected() {
        testFixture.given(bookingCreatedEvent, new BookingSubmitedEvent(bookingId), new BookingRejectedEvent(bookingId))
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(AlreadyRejectedException.class);
    }

    @Test
    public void shouldNotRejectedWhenIsNotSubmitted() {
        testFixture.given(bookingCreatedEvent)
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(NotSubmitedException.class);
    }

    @Test
    public void shouldNotRejectWhenIsAlreadyCanceled() {
        testFixture.given(bookingCreatedEvent, new BookingCanceledEvent(bookingId))
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(AlreadyCanceledException.class);
    }

    @Test
    public void shouldNotRejectWhenIsAlreadyConfirmed() {
        testFixture.given(bookingCreatedEvent, new BookingSubmitedEvent(bookingId), new BookingConfirmedEvent(bookingId))
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(AlreadyConfirmedException.class);
    }

    @Test
    public void shouldReject() {
        testFixture.given(bookingCreatedEvent, new BookingSubmitedEvent(bookingId))
                .when(rejectCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingRejectedEvent event = (BookingRejectedEvent) p.getPayload();
                    return event.getBookingId().equals(bookingId);
                }), andNoMore()));
    }
}
