package domain.booking;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import api.booking.bookingDetail.command.DeleteBookingDetailCommand;
import api.booking.bookingDetail.event.BookingDetailAddedEvent;
import api.booking.bookingDetail.event.BookingDetailDeletedEvent;
import api.booking.command.CancelBookingCommand;
import api.booking.command.ConfirmBookingCommand;
import api.booking.command.CreateBookingCommand;
import api.booking.command.FinishBookingCommand;
import api.booking.command.RejectBookingCommand;
import api.booking.command.SubmitBookingCommand;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingCreatedEvent;
import api.booking.event.BookingFinishedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmittedEvent;
import domain.booking.service.BookingDetailValidator;
import domain.booking.service.BookingValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import query.model.booking.BookingState;

@Aggregate
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Booking {

    @AggregateIdentifier
    private UUID id;
    private BookingState state;
    private Set<UUID> details;

    @CommandHandler
    public Booking(CreateBookingCommand command, BookingValidator validator) {
        validator.validateCreate(command);
        BookingCreatedEvent event = new BookingCreatedEvent();
        copyProperties(command, event);
        event.setBookingId(UUID.randomUUID());
        event.setDate(LocalDateTime.now());
        apply(event);
    }

    @EventSourcingHandler
    public void on(BookingCreatedEvent event) {
        id = event.getBookingId();
        details = new HashSet<>();
        state = BookingState.CREATED;
    }

    @CommandHandler
    public void on(CancelBookingCommand command, BookingValidator validator) {
        validator.assertThatHasValidState(id, state, BookingState.CANCELED,
                EnumSet.of(BookingState.CREATED, BookingState.SUBMITTED, BookingState.CONFIRMED));
        apply(new BookingCanceledEvent(id));
    }

    @EventSourcingHandler
    public void on(BookingCanceledEvent event) {
        state = BookingState.CANCELED;
    }

    @CommandHandler
    public void on(SubmitBookingCommand command, BookingValidator validator) {
        validator.assertThatHasValidState(id, state, BookingState.SUBMITTED, EnumSet.of(BookingState.CREATED));
        validator.assertThatHasAnyBookingDetails(id, details);
        apply(new BookingSubmittedEvent(id));
    }

    @EventSourcingHandler
    public void on(BookingSubmittedEvent event) {
        state = BookingState.SUBMITTED;
    }

    @CommandHandler
    public void on(ConfirmBookingCommand command, BookingValidator validator) {
        validator.assertThatHasValidState(id, state, BookingState.CONFIRMED, EnumSet.of(BookingState.SUBMITTED));
        validator.assertThatHasAnyBookingDetails(id, details);
        apply(new BookingConfirmedEvent(id));
    }

    @EventSourcingHandler
    public void on(BookingConfirmedEvent event) {
        state = BookingState.CONFIRMED;
    }

    @CommandHandler
    public void on(RejectBookingCommand command, BookingValidator validator) {
        validator.assertThatHasValidState(id, state, BookingState.REJECTED, EnumSet.of(BookingState.SUBMITTED));
        apply(new BookingRejectedEvent(id));
    }

    @EventSourcingHandler
    public void on(BookingRejectedEvent event) {
        state = BookingState.REJECTED;
    }

    @CommandHandler
    public void on(FinishBookingCommand command, BookingValidator validator) {
        validator.assertThatHasValidState(id, state, BookingState.FINISHED, EnumSet.of(BookingState.CONFIRMED));
        validator.assertThatHasAnyBookingDetails(id, details);
        apply(new BookingFinishedEvent(id));
    }

    @EventSourcingHandler
    public void on(BookingFinishedEvent event) {
        state = BookingState.FINISHED;
    }

    @CommandHandler
    public void on(AddBookingDetailCommand command, BookingValidator bookingValidator, BookingDetailValidator detailValidator) {
        bookingValidator.assertThatHasValidState(id, state, BookingState.CREATED, EnumSet.of(BookingState.CREATED));
        detailValidator.validate(command, this);
        BookingDetailAddedEvent event = new BookingDetailAddedEvent();
        copyProperties(command, event);
        event.setBookingDetailId(UUID.randomUUID());
        apply(event);
    }

    @EventSourcingHandler
    public void on(BookingDetailAddedEvent event) {
        details.add(event.getBookingDetailId());
    }

    @CommandHandler
    public void on(DeleteBookingDetailCommand command, BookingValidator bookingValidator, BookingDetailValidator detailValidator) {
        bookingValidator.assertThatHasValidState(id, state, BookingState.CREATED, EnumSet.of(BookingState.CREATED));
        detailValidator.validate(command, details);
        apply(new BookingDetailDeletedEvent(command.getBookingDetailId()));
    }

    @EventSourcingHandler
    public void on(BookingDetailDeletedEvent event) {
        details.remove(event.getBookingDetailId());
    }
}
