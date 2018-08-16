package domain.booking;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.UUID;

import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingCreatedEvent;
import api.booking.event.BookingFinishedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmittedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import query.model.booking.BookingEntity;
import query.model.booking.BookingState;
import query.model.booking.repository.BookingEntityRepository;
import query.model.user.UserEntity;
import query.model.user.repository.UserEntityRepository;

@Component
@AllArgsConstructor
public class BookingEntityEventHandler {

    private static final Logger logger = getLogger(BookingEntityEventHandler.class);

    private BookingEntityRepository bookingRepository;
    private UserEntityRepository userRepository;

    @EventHandler
    public void on(BookingCreatedEvent event) {
        UUID bookingId = event.getBookingId();
        logger.info("Creating booking with id: {}", bookingId);
        BookingEntity booking = new BookingEntity();
        copyProperties(event, booking);
        UserEntity customer = userRepository.getOne(event.getCustomerId());
        booking.setCustomer(customer);
        booking.setId(event.getBookingId());
        booking.setState(BookingState.CREATED);
        bookingRepository.save(booking);
    }

    @EventHandler
    public void on(BookingCanceledEvent event) {
        changeStateOfBooking(event.getBookingId(), BookingState.CANCELED);
    }

    @EventHandler
    public void on(BookingSubmittedEvent event) {
        changeStateOfBooking(event.getBookingId(), BookingState.SUBMITTED);
    }

    @EventHandler
    public void on(BookingConfirmedEvent event) {
        changeStateOfBooking(event.getBookingId(), BookingState.CONFIRMED);
    }

    @EventHandler
    public void on(BookingRejectedEvent event) {
        changeStateOfBooking(event.getBookingId(), BookingState.REJECTED);
    }

    @EventHandler
    public void on(BookingFinishedEvent event) {
        changeStateOfBooking(event.getBookingId(), BookingState.FINISHED);
    }

    private void changeStateOfBooking(UUID bookingId, BookingState targetState) {
        BookingEntity booking = bookingRepository.getOne(bookingId);
        logger.info("Changing state of booking with id: {} from state: {} to: {}",
                bookingId,
                booking.getState().name(),
                targetState.name());
        booking.setState(targetState);
        bookingRepository.save(booking);
    }
}
