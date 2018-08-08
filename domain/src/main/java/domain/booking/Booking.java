package domain.booking;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import api.booking.command.CancelBookingCommand;
import api.booking.command.ConfirmBookingCommand;
import api.booking.command.CreateBookingCommand;
import api.booking.command.RejectBookingCommand;
import api.booking.command.SubmitBookingCommand;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingCreatedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmitedEvent;
import domain.booking.service.BookingValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateMember;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Booking {

    @AggregateIdentifier
    private UUID bookingId;
    private boolean canceled;
    private boolean submitted;
    private boolean confirmed;
    private boolean rejected;
    @AggregateMember
    private Set<BookingDetail> bookingDetails;

    @CommandHandler
    public Booking(CreateBookingCommand command, BookingValidator validator) {
        validator.validateCreate(command);
        BookingCreatedEvent event = new BookingCreatedEvent();
        copyProperties(command, event);
        event.setBookingId(UUID.randomUUID());
        event.setBookingDate(LocalDateTime.now());
        apply(event);
    }

    @EventSourcingHandler
    public void on(BookingCreatedEvent event) {
        bookingId = event.getBookingId();
        bookingDetails = new HashSet<>();
    }

    @CommandHandler
    public void on(CancelBookingCommand command, BookingValidator validator) {
        validator.assertThatIsNotCanceled(bookingId, canceled);
        validator.assertThatIsNotSubmitted(bookingId, submitted);
        validator.assertThatIsNotConfirmed(bookingId, confirmed);
        validator.assertThatIsNotRejected(bookingId, rejected);
        apply(new BookingCanceledEvent(bookingId));
    }

    @EventSourcingHandler
    public void on(BookingCanceledEvent event) {
        canceled = true;
    }

    @CommandHandler
    public void on(SubmitBookingCommand command, BookingValidator validator) {
        validator.assertThatIsNotSubmitted(bookingId, submitted);
        validator.assertThatIsNotCanceled(bookingId, canceled);
        validator.assertThatHasAnyBookingDetails(bookingId, bookingDetails);
        validator.assertThatIsNotConfirmed(bookingId, confirmed);
        validator.assertThatIsNotRejected(bookingId, rejected);
        apply(new BookingSubmitedEvent(bookingId));
    }

    @EventSourcingHandler
    public void on(BookingSubmitedEvent event) {
        submitted = true;
    }

    @CommandHandler
    public void on(ConfirmBookingCommand command, BookingValidator validator) {
        validator.assertThatIsNotConfirmed(bookingId, confirmed);
        validator.assertThatIsNotCanceled(bookingId, canceled);
        validator.assertThatHasAnyBookingDetails(bookingId, bookingDetails);
        validator.assertThatIsNotRejected(bookingId, rejected);
        apply(new BookingConfirmedEvent(bookingId));
    }

    @EventSourcingHandler
    public void on(BookingConfirmedEvent event) {
        confirmed = true;
    }

    @CommandHandler
    public void on(RejectBookingCommand command, BookingValidator validator) {
        validator.assertThatIsNotRejected(bookingId, rejected);
        validator.assertThatIsSubmited(bookingId, submitted);
        validator.assertThatIsNotCanceled(bookingId, canceled);
        validator.assertThatIsNotConfirmed(bookingId, confirmed);
        apply(new BookingRejectedEvent(bookingId));
    }

    @EventSourcingHandler
    public void on(BookingRejectedEvent event) {
        rejected = true;
    }
}
