package domain.booking;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;
import static org.mockito.Mockito.when;

import java.util.UUID;

import api.booking.command.CreateBookingCommand;
import api.booking.event.BookingCreatedEvent;
import domain.booking.exception.CustomerNotExistsException;
import org.junit.Test;
import query.model.user.UserType;

public final class CreateBookingTest extends AbstractBookingTest {

    @Test
    public void shouldNotCreateWhenCustomerNotExists() {
        UUID customerId = UUID.randomUUID();
        when(userRepository.existsByIdAndUserTypeAndDeletedFalse(customerId, UserType.CUSTOMER)).thenReturn(false);

        CreateBookingCommand command = CreateBookingCommand.builder()
                .customerId(customerId)
                .build();

        testFixture.givenNoPriorActivity()
                .when(command)
                .expectNoEvents()
                .expectException(CustomerNotExistsException.class);
    }

    @Test
    public void shouldCreateWhenCustomerExists() {
        UUID customerId = UUID.randomUUID();
        when(userRepository.existsByIdAndUserTypeAndDeletedFalse(customerId, UserType.CUSTOMER)).thenReturn(true);

        CreateBookingCommand command = CreateBookingCommand.builder()
                .customerId(customerId)
                .build();

        testFixture.givenNoPriorActivity()
                .when(command)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingCreatedEvent event = (BookingCreatedEvent) p.getPayload();
                    return event.getCustomerId().equals(customerId);
                }), andNoMore()));
    }
}
