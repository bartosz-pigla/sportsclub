package domain.booking;

import java.time.LocalDateTime;
import java.util.UUID;

import api.booking.event.BookingCreatedEvent;
import domain.booking.service.BookingValidator;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import query.repository.BookingDetailEntityRepository;
import query.repository.BookingEntityRepository;
import query.repository.UserEntityRepository;

@RunWith(MockitoJUnitRunner.class)
abstract class AbstractBookingTest {

    protected AggregateTestFixture<Booking> testFixture;
    @Mock
    protected BookingEntityRepository bookingRepository;
    @Mock
    protected BookingDetailEntityRepository bookingDetailRepository;
    @Mock
    protected UserEntityRepository userRepository;

    protected UUID bookingId = UUID.randomUUID();
    protected UUID customerId = UUID.randomUUID();

    protected BookingCreatedEvent createdEvent = BookingCreatedEvent.builder()
            .bookingId(bookingId)
            .customerId(customerId)
            .bookingDate(LocalDateTime.now())
            .build();

    @Before
    public void setUp() {
        testFixture = new AggregateTestFixture<>(Booking.class);
        testFixture.setReportIllegalStateChange(false);
        testFixture.registerInjectableResource(new BookingValidator(userRepository));
    }
}
