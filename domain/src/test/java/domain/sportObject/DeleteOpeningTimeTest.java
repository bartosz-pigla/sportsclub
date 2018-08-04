package domain.sportObject;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import api.sportObject.command.DeleteOpeningTimeCommand;
import api.sportObject.event.OpeningTimeCreatedEvent;
import api.sportObject.event.OpeningTimeDeletedEvent;
import api.sportObject.event.SportObjectCreatedEvent;
import domain.common.exception.AlreadyDeletedException;
import domain.common.exception.NotExistsException;
import org.junit.Test;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.Price;

public final class DeleteOpeningTimeTest extends AbstractSportObjectTest {

    private static SportObjectCreatedEvent sportObjectCreatedEvent = SportObjectCreatedEvent.builder()
            .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
            .description("description1")
            .name("name1")
            .sportObjectId(UUID.randomUUID())
            .sportsclubId(sportsclubCreatedEvent.getSportsclubId())
            .build();

    private static final OpeningTimeCreatedEvent openingTimeCreatedEvent = OpeningTimeCreatedEvent.builder()
            .openingTimeId(UUID.randomUUID())
            .sportObjectId(sportsclubCreatedEvent.getSportsclubId())
            .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(12, 0)))
            .price(new Price(new BigDecimal(11d)))
            .build();

    @Test
    public void shouldNotDeleteWhenNotExists() {
        DeleteOpeningTimeCommand deleteOpeningTimeCommand = DeleteOpeningTimeCommand.builder()
                .openingTimeId(UUID.randomUUID())
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .build();

        testFixture.given(sportObjectCreatedEvent, openingTimeCreatedEvent)
                .when(deleteOpeningTimeCommand)
                .expectException(NotExistsException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldNotDeleteWhenAlreadyDeleted() {
        OpeningTimeDeletedEvent openingTimeDeletedEvent = OpeningTimeDeletedEvent.builder()
                .openingTimeId(openingTimeCreatedEvent.getOpeningTimeId())
                .sportObjectId(openingTimeCreatedEvent.getSportObjectId())
                .build();

        DeleteOpeningTimeCommand deleteOpeningTimeCommand = DeleteOpeningTimeCommand.builder()
                .openingTimeId(openingTimeCreatedEvent.getOpeningTimeId())
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .build();

        testFixture.given(sportObjectCreatedEvent, openingTimeCreatedEvent, openingTimeDeletedEvent)
                .when(deleteOpeningTimeCommand)
                .expectException(AlreadyDeletedException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldDeleteWhenExists() {
        DeleteOpeningTimeCommand deleteOpeningTimeCommand = DeleteOpeningTimeCommand.builder()
                .openingTimeId(openingTimeCreatedEvent.getOpeningTimeId())
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .build();

        testFixture.given(sportObjectCreatedEvent, openingTimeCreatedEvent)
                .when(deleteOpeningTimeCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    OpeningTimeDeletedEvent event = (OpeningTimeDeletedEvent) p.getPayload();
                    return event.getOpeningTimeId().equals(deleteOpeningTimeCommand.getOpeningTimeId());
                }), andNoMore()));
    }
}
