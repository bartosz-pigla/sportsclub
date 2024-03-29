package domain.booking;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import api.booking.event.BookingCreatedEvent;
import domain.booking.service.BookingDetailValidator;
import domain.booking.service.BookingValidator;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import query.model.booking.repository.BookingDetailEntityRepository;
import query.model.booking.repository.BookingEntityRepository;
import query.model.booking.repository.BookingQueryExpressions;
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import query.model.sportobject.repository.SportObjectPositionEntityRepository;
import query.model.user.repository.UserEntityRepository;

@RunWith(MockitoJUnitRunner.Silent.class)
public abstract class AbstractBookingTest {

    protected AggregateTestFixture<Booking> testFixture;
    @Mock
    protected BookingEntityRepository bookingRepository;
    @Mock
    protected BookingDetailEntityRepository bookingDetailRepository;
    @Mock
    protected OpeningTimeEntityRepository openingTimeRepository;
    @Mock
    protected SportObjectPositionEntityRepository sportObjectPositionRepository;
    @Mock
    protected UserEntityRepository userRepository;

    protected UUID bookingId = UUID.randomUUID();
    protected UUID customerId = UUID.randomUUID();

    protected BookingCreatedEvent bookingCreatedEvent = BookingCreatedEvent.builder()
            .bookingId(bookingId)
            .customerId(customerId)
            .date(LocalDateTime.now())
            .build();

    @Before
    public void setUp() {
        testFixture = new AggregateTestFixture<>(Booking.class);
        testFixture.setReportIllegalStateChange(false);
        testFixture.registerInjectableResource(new BookingValidator(userRepository, bookingRepository));
        testFixture.registerInjectableResource(new BookingDetailValidator(
                bookingDetailRepository, openingTimeRepository, sportObjectPositionRepository));

        when(bookingRepository.exists(BookingQueryExpressions.bookingIdAndUserIdMatches(bookingId, customerId))).thenReturn(true);
    }
}
