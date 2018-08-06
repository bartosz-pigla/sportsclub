package domain.booking.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.UUID;

import api.booking.command.CreateBookingCommand;
import domain.booking.exception.CustomerNotExistsException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import query.model.user.UserType;
import query.repository.UserEntityRepository;

@Service
@AllArgsConstructor
public final class CreateBookingValidator {

    private static final Logger logger = getLogger(CreateBookingValidator.class);

    private UserEntityRepository userRepository;

    public void validate(CreateBookingCommand command) {
        UUID customerId = command.getCustomerId();

        if (!userRepository.existsByIdAndUserTypeAndDeletedFalse(customerId, UserType.CUSTOMER)) {
            logger.error("Customer with id: {} not exists", customerId);
            throw new CustomerNotExistsException();
        }
    }
}
