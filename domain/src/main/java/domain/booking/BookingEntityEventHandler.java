package domain.booking;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.UUID;

import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingCreatedEvent;
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
        UUID bookingId = event.getBookingId();
        logger.error("Cancelling booking with id: {}", bookingId);
        BookingEntity booking = bookingRepository.getOne(event.getBookingId());
        booking.setBookingState(BookingState.CANCELED);
        bookingRepository.save(booking);
    }
}
