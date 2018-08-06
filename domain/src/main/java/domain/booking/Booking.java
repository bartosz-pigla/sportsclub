package domain.booking;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import api.booking.command.CancelBookingCommand;
import api.booking.command.CreateBookingCommand;
import api.booking.command.SubmitBookingCommand;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingCreatedEvent;
import domain.booking.service.CancelBookingValidator;
import domain.booking.service.CreateBookingValidator;
import domain.booking.service.SubmitBookingValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateMember;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Booking {

    private UUID bookingId;
    private boolean canceled;
    private boolean submited;
    @AggregateMember
    private Set<BookingDetail> bookingDetails;

    @CommandHandler
    public Booking(CreateBookingCommand command, CreateBookingValidator validator) {
        validator.validate(command);
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
    public void on(CancelBookingCommand command, CancelBookingValidator validator) {
        validator.validate(this);
        apply(new BookingCanceledEvent(command.getBookingId()));
    }

    @EventSourcingHandler
    public void on(BookingCanceledEvent event) {
        canceled = true;
    }

    @CommandHandler
    public void on(SubmitBookingCommand command, SubmitBookingValidator validator) {
        validator.validate(this);

    }
}
