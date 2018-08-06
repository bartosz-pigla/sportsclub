package domain.booking.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.UUID;

import domain.booking.Booking;
import domain.booking.exception.AlreadyCancelledException;
import domain.booking.exception.NotFoundAnyBookingDetailsException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class SubmitBookingValidator {

    private static final Logger logger = getLogger(SubmitBookingValidator.class);

    public void validate(Booking booking) {
        UUID bookingId = booking.getBookingId();

        if(booking.isCanceled()) {
            logger.error("Booking with id: {} is already cancelled", bookingId);
            throw new AlreadyCancelledException();
        }



        if (booking.getBookingDetails().isEmpty()) {
            logger.error("Booking with id: {}", booking.getBookingId());
            throw new NotFoundAnyBookingDetailsException();
        }
    }
}
