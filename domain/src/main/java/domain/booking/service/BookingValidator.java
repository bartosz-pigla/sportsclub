package domain.booking.service;

import static org.slf4j.LoggerFactory.getLogger;
import static query.model.user.repository.UserQueryExpressions.idAndUserTypeMatches;

import java.util.Collection;
import java.util.EnumSet;
import java.util.UUID;

import api.booking.command.CreateBookingCommand;
import domain.booking.exception.CustomerNotExistsException;
import domain.booking.exception.IllegalBookingStateException;
import domain.booking.exception.NotContainsAnyBookingDetailException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import query.model.booking.BookingState;
import query.model.user.UserType;
import query.model.user.repository.UserEntityRepository;

@Service
@AllArgsConstructor
public final class BookingValidator {

    private static final Logger logger = getLogger(BookingValidator.class);

    private UserEntityRepository userRepository;

    public void validateCreate(CreateBookingCommand command) {
        UUID customerId = command.getCustomerId();

        if (!userRepository.exists(idAndUserTypeMatches(customerId, UserType.CUSTOMER))) {
            logger.error("Customer with id: {} not exists", customerId);
            throw new CustomerNotExistsException();
        }
    }

    public void assertThatHasValidState(UUID bookingId, BookingState currentState, BookingState targetState, EnumSet<BookingState> allowedStates) {
        if (!allowedStates.contains(currentState)) {
            logger.error("Cannot change state of booking with id: {} from state: {} to state: {}",
                    bookingId, currentState.name(), targetState.name());
            throw new IllegalBookingStateException(currentState, targetState);
        }
    }

    public void assertThatHasAnyBookingDetails(UUID bookingId, Collection<UUID> details) {
        if (details.isEmpty()) {
            logger.error("Booking with id: {}", bookingId);
            throw new NotContainsAnyBookingDetailException();
        }
    }
}
