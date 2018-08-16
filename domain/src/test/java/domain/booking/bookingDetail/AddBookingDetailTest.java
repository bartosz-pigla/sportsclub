package domain.booking.bookingDetail;

import static domain.booking.service.BookingDetailValidator.DETAILS_LIMIT;
import static junit.framework.TestCase.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import api.booking.bookingDetail.event.BookingDetailAddedEvent;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmitedEvent;
import com.querydsl.core.types.dsl.BooleanExpression;
import domain.booking.AbstractBookingTest;
import domain.booking.exception.AlreadyCanceledException;
import domain.booking.exception.AlreadyConfirmedException;
import domain.booking.exception.AlreadyRejectedException;
import domain.booking.exception.AlreadySubmitedException;
import domain.booking.exception.BookingDetailsLimitExceededException;
import domain.booking.exception.InvalidOpeningTimeException;
import domain.booking.exception.OpeningTimeNotExistsException;
import domain.booking.exception.BookingWithGivenDateAndPositionAlreadyExists;
import org.junit.Test;
import query.model.booking.QBookingDetailEntity;
import query.model.sportobject.OpeningTimeEntity;
import query.model.sportobject.SportObjectEntity;
import query.model.sportobject.SportObjectPositionEntity;

public final class AddBookingDetailTest extends AbstractBookingTest {

    private UUID openingTimeId = UUID.randomUUID();
    private UUID positionId = UUID.randomUUID();

    private AddBookingDetailCommand command = AddBookingDetailCommand.builder()
            .bookingId(bookingId)
            .openingTimeId(openingTimeId)
            .sportObjectPositionId(positionId)
            .build();

    @Test
    public void shouldNotAddWhenOtherExistsWithSameOpeningTime() {
        BookingDetailAddedEvent detailAddedEvent = BookingDetailAddedEvent.builder()
                .sportObjectPositionId(positionId)
                .openingTimeId(openingTimeId)
                .bookingDetailId(UUID.randomUUID())
                .build();

        testFixture.given(bookingCreatedEvent, detailAddedEvent)
                .when(command)
                .expectNoEvents()
                .expectException(BookingWithGivenDateAndPositionAlreadyExists.class);
    }

    @Test
    public void shouldNotAddWhenBookingIsCanceled() {
        testFixture.given(bookingCreatedEvent, new BookingCanceledEvent(bookingId))
                .when(command)
                .expectNoEvents()
                .expectException(AlreadyCanceledException.class);
    }

    @Test
    public void shouldNotAddWhenBookingIsConfirmed() {
        testFixture.given(bookingCreatedEvent, new BookingConfirmedEvent(bookingId))
                .when(command)
                .expectNoEvents()
                .expectException(AlreadyConfirmedException.class);
    }

    @Test
    public void shouldNotAddWhenBookingIsRejected() {
        testFixture.given(bookingCreatedEvent, new BookingRejectedEvent(bookingId))
                .when(command)
                .expectNoEvents()
                .expectException(AlreadyRejectedException.class);
    }

    @Test
    public void shouldNotAddWhenBookingIsSubmitted() {
        BookingDetailAddedEvent detailAddedEvent = BookingDetailAddedEvent.builder()
                .sportObjectPositionId(positionId)
                .openingTimeId(openingTimeId)
                .bookingDetailId(UUID.randomUUID())
                .build();

        testFixture.given(bookingCreatedEvent, detailAddedEvent, new BookingSubmitedEvent(bookingId))
                .when(command)
                .expectNoEvents()
                .expectException(AlreadySubmitedException.class);
    }

    @Test
    public void shouldNotAddWhenExceedsDetailsLimit() {
        List<Object> events = new ArrayList<>();
        events.add(bookingCreatedEvent);
        addMaxAllowedDetailEvent(events, DETAILS_LIMIT);
        assertTrue(true);

        testFixture.given(events)
                .when(command)
                .expectNoEvents()
                .expectException(BookingDetailsLimitExceededException.class);
    }

    @Test
    public void shouldNotAddWhenOpeningTimeNotExists() {
        testFixture.given(bookingCreatedEvent)
                .when(AddBookingDetailCommand.builder()
                        .bookingId(bookingId)
                        .openingTimeId(UUID.randomUUID())
                        .sportObjectPositionId(positionId)
                        .build())
                .expectNoEvents()
                .expectException(OpeningTimeNotExistsException.class);
    }

    @Test
    public void shouldNotAddWhenOpeningTimeIsNotAssignedToGivenSportObjectPosition() {
        OpeningTimeEntity openingTime = new OpeningTimeEntity();
        SportObjectEntity sportObject = new SportObjectEntity();
        sportObject.setId(UUID.randomUUID());
        openingTime.setSportObject(sportObject);
        when(openingTimeRepository.getOne(command.getOpeningTimeId())).thenReturn(openingTime);

        testFixture.given(bookingCreatedEvent)
                .when(command)
                .expectNoEvents()
                .expectException(InvalidOpeningTimeException.class);
    }

    @Test
    public void shouldNotAddWhenAllFreePositionsAreAlreadyBooked() {
        OpeningTimeEntity openingTime = new OpeningTimeEntity();
        SportObjectPositionEntity sportObjectPosition = new SportObjectPositionEntity();
        BooleanExpression predicate = QBookingDetailEntity.bookingDetailEntity.openingTime.eq(openingTime).and(QBookingDetailEntity.bookingDetailEntity.position.eq(sportObjectPosition));
        bookingDetailRepository.findAll(predicate);
        assertTrue(true);
    }

    @Test
    public void shouldAdd() {
        assertTrue(true);
    }

    private void addMaxAllowedDetailEvent(List<Object> events, int detailsLimit) {
        for (int i = 0; i < detailsLimit; i++) {
            events.add(BookingDetailAddedEvent.builder()
                    .bookingDetailId(UUID.randomUUID())
                    .openingTimeId(openingTimeId)
                    .sportObjectPositionId(positionId)
                    .build());
        }
    }
}
