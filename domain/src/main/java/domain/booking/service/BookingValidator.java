package domain.booking.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.UUID;

import api.booking.command.CreateBookingCommand;
import domain.booking.BookingDetail;
import domain.booking.exception.AlreadyCanceledException;
import domain.booking.exception.AlreadyConfirmedException;
import domain.booking.exception.AlreadyRejectedException;
import domain.booking.exception.AlreadySubmitedException;
import domain.booking.exception.CustomerNotExistsException;
import domain.booking.exception.BookingDetailsNotExistsException;
import domain.booking.exception.NotSubmitedException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import query.model.user.UserType;
import query.repository.UserEntityRepository;

@Service
@AllArgsConstructor
public final class BookingValidator {

    private static final Logger logger = getLogger(BookingValidator.class);

    private UserEntityRepository userRepository;

    public void validateCreate(CreateBookingCommand command) {
        UUID customerId = command.getCustomerId();

        if (!userRepository.existsByIdAndUserTypeAndDeletedFalse(customerId, UserType.CUSTOMER)) {
            logger.error("Customer with id: {} not exists", customerId);
            throw new CustomerNotExistsException();
        }
    }

    public void assertThatIsNotCanceled(UUID bookingId, boolean canceled) {
        if (canceled) {
            logger.error("Booking with id: {} is already canceled", bookingId);
            throw new AlreadyCanceledException();
        }
    }

    public void assertThatIsNotSubmitted(UUID bookingId, boolean submitted) {
        if (submitted) {
            logger.error("Booking with id: {} is already submited", bookingId);
            throw new AlreadySubmitedException();
        }
    }

    public void assertThatIsSubmited(UUID bookingId, boolean submitted) {
        if (!submitted) {
            logger.error("Booking with id: {} is not submited", bookingId);
            throw new NotSubmitedException();
        }
    }

    public void assertThatHasAnyBookingDetails(UUID bookingId, Collection<BookingDetail> bookingDetails) {
        if (bookingDetails.isEmpty()) {
            logger.error("Booking with id: {}", bookingId);
            throw new BookingDetailsNotExistsException();
        }
    }

    public void assertThatIsNotConfirmed(UUID bookingId, boolean confirmed) {
        if (confirmed) {
            logger.error("Booking with id: {} is already confirmed", bookingId);
            throw new AlreadyConfirmedException();
        }
    }

    public void assertThatIsNotRejected(UUID bookingId, boolean rejected) {
        if (rejected) {
            logger.error("Booking with id: {} is already rejected", bookingId);
            throw new AlreadyRejectedException();
        }
    }
}
