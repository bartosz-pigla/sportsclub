package domain.booking;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import api.booking.command.RejectBookingCommand;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmitedEvent;
import domain.booking.exception.AlreadyConfirmedException;
import domain.booking.exception.AlreadyRejectedException;
import domain.booking.exception.AlreadySubmitedException;
import domain.booking.exception.NotSubmitedException;
import org.junit.Test;

public final class RejectBookingTest extends AbstractBookingTest {

    private RejectBookingCommand rejectCommand = RejectBookingCommand.builder()
            .bookingId(bookingId)
            .build();

    @Test
    public void shouldNotRejectWhenIsAlreadyRejected() {
        testFixture.given(createdEvent, new BookingSubmitedEvent(bookingId), new BookingRejectedEvent(bookingId))
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(AlreadyRejectedException.class);
    }

    @Test
    public void shouldNotRejectedWhenIsNotSubmited() {
        testFixture.given(createdEvent)
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(NotSubmitedException.class);
    }

    @Test
    public void shouldNotRejectWhenIsAlreadyCanceled() {
        testFixture.given(createdEvent, new BookingCanceledEvent(bookingId))
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(AlreadySubmitedException.class);
    }

    @Test
    public void shouldNotRejectWhenIsAlreadyConfirmed() {
        testFixture.given(createdEvent, new BookingSubmitedEvent(bookingId), new BookingConfirmedEvent(bookingId))
                .when(rejectCommand)
                .expectNoEvents()
                .expectException(AlreadyConfirmedException.class);
    }

    @Test
    public void shouldReject() {
        testFixture.given(createdEvent)
                .when(rejectCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingRejectedEvent event = (BookingRejectedEvent) p.getPayload();
                    return event.getBookingId().equals(bookingId);
                }), andNoMore()));
    }
}
