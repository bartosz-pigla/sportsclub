package domain.sportObject;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import api.sportObject.command.UpdateOpeningTimeCommand;
import api.sportObject.event.OpeningTimeCreatedEvent;
import api.sportObject.event.OpeningTimeUpdatedEvent;
import api.sportObject.event.SportObjectCreatedEvent;
import domain.common.exception.NotExistsException;
import domain.sportObject.exception.OpeningTimeRangeConflictException;
import org.junit.Test;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.Price;

public final class UpdateOpeningTimeTest extends AbstractSportObjectTest {

    private static SportObjectCreatedEvent sportObjectCreatedEvent = SportObjectCreatedEvent.builder()
            .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
            .description("description1")
            .name("name1")
            .sportObjectId(UUID.randomUUID())
            .sportsclubId(sportsclubCreatedEvent.getSportsclubId())
            .build();

    private static final OpeningTimeCreatedEvent firstOpeningTimeCreatedEvent = OpeningTimeCreatedEvent.builder()
            .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
            .openingTimeId(UUID.randomUUID())
            .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0)))
            .price(new Price(new BigDecimal(11d)))
            .build();

    private static final OpeningTimeCreatedEvent openingTimeCreatedEvent = OpeningTimeCreatedEvent.builder()
            .openingTimeId(UUID.randomUUID())
            .sportObjectId(sportsclubCreatedEvent.getSportsclubId())
            .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(12, 0)))
            .price(new Price(new BigDecimal(11d)))
            .build();

    private static final OpeningTimeCreatedEvent lastOpeningTimeCreatedEvent = OpeningTimeCreatedEvent.builder()
            .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
            .openingTimeId(UUID.randomUUID())
            .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(13, 0), LocalTime.of(14, 0)))
            .price(new Price(new BigDecimal(11d)))
            .build();

    @Test
    public void shouldNotUpdateWhenOpeningTimeNotExists() {
        UpdateOpeningTimeCommand updateOpeningTimeCommand = UpdateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .openingTimeId(UUID.randomUUID())
                .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(12, 0)))
                .price(new Price(new BigDecimal(20d)))
                .build();

        testFixture.given(sportObjectCreatedEvent, firstOpeningTimeCreatedEvent, openingTimeCreatedEvent, lastOpeningTimeCreatedEvent)
                .when(updateOpeningTimeCommand)
                .expectException(NotExistsException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldNotUpdateWhenStartOpeningTimeIsInExistingRange() {
        UpdateOpeningTimeCommand updateOpeningTimeCommand = UpdateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .openingTimeId(openingTimeCreatedEvent.getOpeningTimeId())
                .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(9, 30), LocalTime.of(13, 0)))
                .price(new Price(new BigDecimal(20d)))
                .build();

        testFixture.given(sportObjectCreatedEvent, firstOpeningTimeCreatedEvent, openingTimeCreatedEvent, lastOpeningTimeCreatedEvent)
                .when(updateOpeningTimeCommand)
                .expectException(OpeningTimeRangeConflictException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldNotUpdateWhenFinishOpeningTimeIsInExistingRange() {
        UpdateOpeningTimeCommand updateOpeningTimeCommand = UpdateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .openingTimeId(openingTimeCreatedEvent.getOpeningTimeId())
                .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(13, 30)))
                .price(new Price(new BigDecimal(20d)))
                .build();

        testFixture.given(sportObjectCreatedEvent, firstOpeningTimeCreatedEvent, openingTimeCreatedEvent, lastOpeningTimeCreatedEvent)
                .when(updateOpeningTimeCommand)
                .expectException(OpeningTimeRangeConflictException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldNotUpdateWhenBothStartAndFinishOpeningTimeAreInExistingRange() {
        UpdateOpeningTimeCommand updateOpeningTimeCommand = UpdateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .openingTimeId(openingTimeCreatedEvent.getOpeningTimeId())
                .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(9, 30), LocalTime.of(13, 30)))
                .price(new Price(new BigDecimal(20d)))
                .build();

        testFixture.given(sportObjectCreatedEvent, firstOpeningTimeCreatedEvent, openingTimeCreatedEvent, lastOpeningTimeCreatedEvent)
                .when(updateOpeningTimeCommand)
                .expectException(OpeningTimeRangeConflictException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldUpdateWhenBothStartAndFinishOpeningTimeAreNotInExistingRange() {
        UpdateOpeningTimeCommand updateOpeningTimeCommand = UpdateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .openingTimeId(openingTimeCreatedEvent.getOpeningTimeId())
                .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(13, 0)))
                .price(new Price(new BigDecimal(20d)))
                .build();

        testFixture.given(sportObjectCreatedEvent, firstOpeningTimeCreatedEvent, openingTimeCreatedEvent, lastOpeningTimeCreatedEvent)
                .when(updateOpeningTimeCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    OpeningTimeUpdatedEvent event = (OpeningTimeUpdatedEvent) p.getPayload();
                    return event.getOpeningTimeId().equals(openingTimeCreatedEvent.getOpeningTimeId());
                }), andNoMore()));
    }
}
