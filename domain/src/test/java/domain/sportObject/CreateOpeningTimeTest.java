package domain.sportObject;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import api.sportObject.command.CreateOpeningTimeCommand;
import api.sportObject.event.OpeningTimeCreatedEvent;
import domain.sportObject.exception.OpeningTimeRangeConflictException;
import org.junit.Test;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.Price;

public final class CreateOpeningTimeTest extends AbstractSportObjectTest {

    private static final OpeningTimeCreatedEvent openingTimeCreatedEvent = OpeningTimeCreatedEvent.builder()
            .openingTimeId(UUID.randomUUID())
            .sportObjectId(sportsclubCreatedEvent.getSportsclubId())
            .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(12, 0)))
            .price(new Price(new BigDecimal(11d)))
            .build();

    @Test
    public void shouldNotCreateWhenStartOpeningTimeIsInExistingRange() {
        CreateOpeningTimeCommand command = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(11, 30), LocalTime.of(12, 30)))
                .price(new Price(new BigDecimal(20d)))
                .build();

        testFixture.given(sportObjectCreatedEvent, openingTimeCreatedEvent)
                .when(command)
                .expectException(OpeningTimeRangeConflictException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldNotCreateWhenFinishOpeningTimeIsInExistingRange() {
        CreateOpeningTimeCommand command = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)))
                .price(new Price(new BigDecimal(20d)))
                .build();

        testFixture.given(sportObjectCreatedEvent, openingTimeCreatedEvent)
                .when(command)
                .expectException(OpeningTimeRangeConflictException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldNotCreateWhenBothStartAndFinishOpeningTimeIsInExistingRange() {
        CreateOpeningTimeCommand command = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(11, 20), LocalTime.of(11, 40)))
                .price(new Price(new BigDecimal(20d)))
                .build();

        testFixture.given(sportObjectCreatedEvent, openingTimeCreatedEvent)
                .when(command)
                .expectException(OpeningTimeRangeConflictException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldCreateWhenBothStartAndFinishOpeningTimeIsNotInExistingRangeButIsInOtherDay() {
        CreateOpeningTimeCommand command = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .dateRange(new OpeningTimeRange(DayOfWeek.TUESDAY, LocalTime.of(11, 20), LocalTime.of(11, 40)))
                .price(new Price(new BigDecimal(20d)))
                .build();

        testFixture.given(sportObjectCreatedEvent, openingTimeCreatedEvent)
                .when(command)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    OpeningTimeCreatedEvent event = (OpeningTimeCreatedEvent) p.getPayload();
                    return event.getSportObjectId().equals(sportObjectCreatedEvent.getSportObjectId());
                }), andNoMore()));
    }

    @Test
    public void shouldCreateWhenBothStartAndFinishOpeningTimeIsNotInExistingRange() {
        CreateOpeningTimeCommand command = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(13, 20), LocalTime.of(14, 40)))
                .price(new Price(new BigDecimal(20d)))
                .build();

        testFixture.given(sportObjectCreatedEvent, openingTimeCreatedEvent)
                .when(command)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    OpeningTimeCreatedEvent event = (OpeningTimeCreatedEvent) p.getPayload();
                    return event.getSportObjectId().equals(sportObjectCreatedEvent.getSportObjectId());
                }), andNoMore()));
    }
}
