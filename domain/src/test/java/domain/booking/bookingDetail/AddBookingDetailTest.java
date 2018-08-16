package domain.booking.bookingDetail;

import static domain.booking.service.BookingDetailValidator.DETAILS_LIMIT;
import static junit.framework.TestCase.assertTrue;
import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;
import static org.mockito.Mockito.when;
import static query.model.booking.repository.BookingDetailQueryExpressions.bookingDetailMatches;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import api.booking.bookingDetail.event.BookingDetailAddedEvent;
import api.booking.event.BookingCanceledEvent;
import api.booking.event.BookingConfirmedEvent;
import api.booking.event.BookingFinishedEvent;
import api.booking.event.BookingRejectedEvent;
import api.booking.event.BookingSubmittedEvent;
import domain.booking.AbstractBookingTest;
import domain.booking.exception.AllSportObjectPositionsAlreadyBookedException;
import domain.booking.exception.BookingDetailsLimitExceededException;
import domain.booking.exception.IllegalBookingStateException;
import domain.booking.exception.InvalidOpeningTimeAssignException;
import domain.sportObject.exception.OpeningTimeNotExistsException;
import org.junit.Test;
import query.model.embeddable.PositionsCount;
import query.model.sportobject.OpeningTimeEntity;
import query.model.sportobject.SportObjectEntity;
import query.model.sportobject.SportObjectPositionEntity;
import query.model.sportobject.repository.OpeningTimeQueryExpressions;
import query.model.sportobject.repository.SportObjectPositionQueryExpressions;

public final class AddBookingDetailTest extends AbstractBookingTest {

    private UUID openingTimeId = UUID.randomUUID();
    private UUID positionId = UUID.randomUUID();

    private AddBookingDetailCommand command = AddBookingDetailCommand.builder()
            .bookingId(bookingId)
            .openingTimeId(openingTimeId)
            .sportObjectPositionId(positionId)
            .build();

    @Test
    public void shouldNotAddWhenBookingIsCanceled() {
        testFixture.given(bookingCreatedEvent, new BookingCanceledEvent(bookingId))
                .when(command)
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotAddWhenBookingIsConfirmed() {
        testFixture.given(bookingCreatedEvent, new BookingConfirmedEvent(bookingId))
                .when(command)
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotAddWhenBookingIsRejected() {
        testFixture.given(bookingCreatedEvent, new BookingRejectedEvent(bookingId))
                .when(command)
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotAddWhenBookingIsSubmitted() {
        BookingDetailAddedEvent detailAddedEvent = BookingDetailAddedEvent.builder()
                .sportObjectPositionId(positionId)
                .openingTimeId(openingTimeId)
                .bookingDetailId(UUID.randomUUID())
                .build();

        testFixture.given(bookingCreatedEvent, detailAddedEvent, new BookingSubmittedEvent(bookingId))
                .when(command)
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
    }

    @Test
    public void shouldNotAddWhenBookingIsFinished() {
        BookingDetailAddedEvent detailAddedEvent = BookingDetailAddedEvent.builder()
                .sportObjectPositionId(positionId)
                .openingTimeId(openingTimeId)
                .bookingDetailId(UUID.randomUUID())
                .build();

        testFixture.given(
                bookingCreatedEvent,
                detailAddedEvent,
                new BookingSubmittedEvent(bookingId),
                new BookingConfirmedEvent(bookingId),
                new BookingFinishedEvent(bookingId))
                .when(command)
                .expectNoEvents()
                .expectException(IllegalBookingStateException.class);
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

        when(openingTimeRepository
                .findOne(OpeningTimeQueryExpressions.idMatches(command.getOpeningTimeId())))
                .thenReturn(Optional.of(openingTime));

        SportObjectPositionEntity sportObjectPosition = new SportObjectPositionEntity();
        sportObjectPosition.setSportObject(new SportObjectEntity());
        when(sportObjectPositionRepository
                .findOne(SportObjectPositionQueryExpressions.idMatches(command.getSportObjectPositionId())))
                .thenReturn(Optional.of(sportObjectPosition));

        testFixture.given(bookingCreatedEvent)
                .when(command)
                .expectNoEvents()
                .expectException(InvalidOpeningTimeAssignException.class);
    }

    @Test
    public void shouldNotAddWhenAllFreePositionsAreAlreadyBooked() {
        SportObjectEntity sportObject = new SportObjectEntity();

        UUID openingTimeId = command.getOpeningTimeId();
        OpeningTimeEntity openingTime = new OpeningTimeEntity();
        openingTime.setId(openingTimeId);
        openingTime.setSportObject(sportObject);

        when(openingTimeRepository
                .findOne(OpeningTimeQueryExpressions.idMatches(openingTimeId)))
                .thenReturn(Optional.of(openingTime));

        UUID sportObjectPositionId = command.getSportObjectPositionId();
        SportObjectPositionEntity sportObjectPosition = new SportObjectPositionEntity();
        sportObjectPosition.setId(sportObjectPositionId);
        sportObjectPosition.setPositionsCount(new PositionsCount(1));
        sportObjectPosition.setSportObject(sportObject);

        when(sportObjectPositionRepository
                .findOne(SportObjectPositionQueryExpressions.idMatches(sportObjectPositionId)))
                .thenReturn(Optional.of(sportObjectPosition));

        when(bookingDetailRepository
                .count(bookingDetailMatches(sportObjectPositionId, command.getDate(), openingTime.getTimeRange())))
                .thenReturn(1L);

        BookingDetailAddedEvent detailAddedEvent = BookingDetailAddedEvent.builder()
                .bookingDetailId(UUID.randomUUID())
                .openingTimeId(openingTimeId)
                .sportObjectPositionId(sportObjectPositionId)
                .date(LocalDate.now())
                .build();

        testFixture.given(bookingCreatedEvent, detailAddedEvent)
                .when(AddBookingDetailCommand.builder()
                        .bookingId(bookingId)
                        .date(LocalDate.now().plusDays(1))
                        .openingTimeId(openingTimeId)
                        .sportObjectPositionId(sportObjectPositionId)
                        .build())
                .expectNoEvents()
                .expectException(AllSportObjectPositionsAlreadyBookedException.class);
    }

    @Test
    public void shoulAdd() {
        SportObjectEntity sportObject = new SportObjectEntity();

        UUID openingTimeId = command.getOpeningTimeId();
        OpeningTimeEntity openingTime = new OpeningTimeEntity();
        openingTime.setId(openingTimeId);
        openingTime.setSportObject(sportObject);

        when(openingTimeRepository
                .findOne(OpeningTimeQueryExpressions.idMatches(openingTimeId)))
                .thenReturn(Optional.of(openingTime));

        UUID sportObjectPositionId = command.getSportObjectPositionId();
        SportObjectPositionEntity sportObjectPosition = new SportObjectPositionEntity();
        sportObjectPosition.setId(sportObjectPositionId);
        sportObjectPosition.setPositionsCount(new PositionsCount(2));
        sportObjectPosition.setSportObject(sportObject);

        when(sportObjectPositionRepository
                .findOne(SportObjectPositionQueryExpressions.idMatches(sportObjectPositionId)))
                .thenReturn(Optional.of(sportObjectPosition));

        when(bookingDetailRepository
                .count(bookingDetailMatches(sportObjectPositionId, command.getDate(), openingTime.getTimeRange())))
                .thenReturn(1L);

        BookingDetailAddedEvent detailAddedEvent = BookingDetailAddedEvent.builder()
                .bookingDetailId(UUID.randomUUID())
                .openingTimeId(openingTimeId)
                .sportObjectPositionId(sportObjectPositionId)
                .date(LocalDate.now())
                .build();

        testFixture.given(bookingCreatedEvent, detailAddedEvent)
                .when(AddBookingDetailCommand.builder()
                        .bookingId(bookingId)
                        .date(LocalDate.now().plusDays(1))
                        .openingTimeId(openingTimeId)
                        .sportObjectPositionId(sportObjectPositionId)
                        .build())
                .expectEventsMatching(sequenceOf(matches(p -> {
                    BookingDetailAddedEvent event = (BookingDetailAddedEvent) p.getPayload();
                    return event.getOpeningTimeId().equals(openingTimeId) && event.getSportObjectPositionId().equals(sportObjectPositionId);
                }), andNoMore()));
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
