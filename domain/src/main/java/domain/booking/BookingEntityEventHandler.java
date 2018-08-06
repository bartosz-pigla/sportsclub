package domain.booking;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.UUID;

import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingCreatedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmitedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import query.model.booking.BookingEntity;
import query.model.booking.BookingState;
import query.model.user.UserEntity;
import query.repository.BookingEntityRepository;
import query.repository.UserEntityRepository;

@Component
@AllArgsConstructor
public class BookingEntityEventHandler {

    private static final Logger logger = getLogger(BookingEntityEventHandler.class);

    private BookingEntityRepository bookingRepository;
    private UserEntityRepository userRepository;

    @EventHandler
    public void on(BookingCreatedEvent event) {
        UUID bookingId = event.getBookingId();
        logger.error("Creating booking with id: {}", bookingId);
        BookingEntity booking = new BookingEntity();
        copyProperties(event, booking);
        UserEntity customer = userRepository.getOne(event.getCustomerId());
        booking.setCustomer(customer);
        booking.setId(event.getBookingId());
        booking.setBookingState(BookingState.CREATED);
        bookingRepository.save(booking);
    }

    @EventHandler
    public void on(BookingCanceledEvent event) {
        changeStateOfBooking(event.getBookingId(), BookingState.CANCELED);
    }

    @EventHandler
    public void on(BookingSubmitedEvent event) {
        changeStateOfBooking(event.getBookingId(), BookingState.SUBMITED);
    }

    @EventHandler
    public void on(BookingConfirmedEvent event) {
        changeStateOfBooking(event.getBookingId(), BookingState.CONFIRMED);
    }

    @EventHandler
    public void on(BookingRejectedEvent event) {
        changeStateOfBooking(event.getBookingId(), BookingState.REJECTED);
    }

    private void changeStateOfBooking(UUID bookingId, BookingState targetState) {
        BookingEntity booking = bookingRepository.getOne(bookingId);
        logger.error("Changing state of booking with id: {} from state: {} to: {}",
                bookingId,
                booking.getBookingState().name(),
                targetState.name());
        booking.setBookingState(targetState);
        bookingRepository.save(booking);
    }
}
