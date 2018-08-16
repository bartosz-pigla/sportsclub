package domain.booking;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.time.LocalDate;
import java.util.UUID;

import api.booking.bookingDetail.event.BookingDetailAddedEvent;
import api.booking.command.SubmitBookingCommand;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingFinishedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmittedEvent;
import domain.booking.exception.IllegalBookingStateException;
import domain.booking.exception.NotContainsAnyBookingDetailException;
import org.junit.Test;

public final class SubmitBookingTest extends AbstractBookingTest {

    private BookingDetailAddedEvent detailAddedEvent = BookingDetailAddedEvent.builder()
            .bookingDetailId(UUID.randomUUID())
            .openingTimeId(UUID.randomUUID())
            .sportObjectPositionId(UUID.randomUUID())
            .date(LocalDate.now())
            .build();

    @Test
    public void shouldNotSubmitWhenIsAlreadySubmitted() {
        testFixture.given(bookingCreatedEvent, detailAddedEvent, new BookingSubmittedEvent(bookingId))
                .when(new SubmitBookingCommand(bookingId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotSubmitWhenIsAlreadyCanceled() {
        testFixture.given(bookingCreatedEvent, detailAddedEvent, new BookingCanceledEvent(bookingId))
                .when(new SubmitBookingCommand(bookingId))
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotSubmitWhenIsAlreadyRejected() {
        testFixture.given(bookingCreatedEvent, detailAddedEvent, new BookingSubmittedEvent(bookingId), new BookingRejectedEvent(bookingId))
                .when(new SubmitBookingCommand(bookingId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotSubmitWhenIsAlreadyConfirmed() {
        testFixture.given(bookingCreatedEvent, detailAddedEvent, new BookingSubmittedEvent(bookingId), new BookingConfirmedEvent(bookingId))
                .when(new SubmitBookingCommand(bookingId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotSubmitWhenIsAlreadyFinished() {
        testFixture.given(
                bookingCreatedEvent,
                detailAddedEvent,
                new BookingSubmittedEvent(bookingId),
                new BookingConfirmedEvent(bookingId),
                new BookingFinishedEvent(bookingId))
                .when(new SubmitBookingCommand(bookingId))
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotSubmitWhenNotContainsAnyBookingDetail() {
        testFixture.given(bookingCreatedEvent)
                .when(new SubmitBookingCommand(bookingId))
                .expectNoEvents()
                .expectException(NotContainsAnyBookingDetailException.class);
    }

    @Test
    public void shouldSubmit() {
        testFixture.given(bookingCreatedEvent, detailAddedEvent)
                .when(new SubmitBookingCommand(bookingId))
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingSubmittedEvent event = (BookingSubmittedEvent) p.getPayload();
                    return event.getBookingId().equals(bookingId);
                }), andNoMore()));
    }
}
