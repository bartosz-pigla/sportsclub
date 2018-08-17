package domain.booking;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.time.LocalDate;
import java.util.UUID;

import api.booking.bookingDetail.event.BookingDetailAddedEvent;
import api.booking.command.FinishBookingCommand;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingFinishedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmittedEvent;
import domain.booking.exception.IllegalBookingStateException;
import org.junit.Test;

public final class FinishBookingTest extends AbstractBookingTest {

    @Test
    public void shouldNotFinishedWhenIsRejected() {
        testFixture.given(bookingCreatedEvent, new BookingSubmittedEvent(bookingId), new BookingRejectedEvent(bookingId))
                .when(new FinishBookingCommand(bookingId, customerId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotFinishWhenIsNotConfirmed() {
        testFixture.given(bookingCreatedEvent, new BookingSubmittedEvent(bookingId))
                .when(new FinishBookingCommand(bookingId, customerId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotFinishWhenIsNotSubmitted() {
        testFixture.given(bookingCreatedEvent)
                .when(new FinishBookingCommand(bookingId, customerId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotFinishWhenIsCanceled() {
        testFixture.given(bookingCreatedEvent, new BookingCanceledEvent(bookingId))
                .when(new FinishBookingCommand(bookingId, customerId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldFinish() {
        BookingDetailAddedEvent detailAddedEvent = BookingDetailAddedEvent.builder()
                .bookingDetailId(UUID.randomUUID())
                .openingTimeId(UUID.randomUUID())
                .sportObjectPositionId(UUID.randomUUID())
                .date(LocalDate.now())
                .build();

        testFixture.given(bookingCreatedEvent, detailAddedEvent, new BookingSubmittedEvent(bookingId), new BookingConfirmedEvent(bookingId))
                .when(new FinishBookingCommand(bookingId, customerId))
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingFinishedEvent event = (BookingFinishedEvent) p.getPayload();
                    return event.getBookingId().equals(bookingId);
                }), andNoMore()));
    }
}
