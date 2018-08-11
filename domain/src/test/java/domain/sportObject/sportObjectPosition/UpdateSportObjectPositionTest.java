package domain.sportObject.sportObjectPosition;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.util.UUID;

import api.sportObject.sportObjectPosition.command.UpdateSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.event.SportObjectPositionCreatedEvent;
import api.sportObject.sportObjectPosition.event.SportObjectPositionDeletedEvent;
import api.sportObject.sportObjectPosition.event.SportObjectPositionUpdatedEvent;
import domain.common.exception.AlreadyDeletedException;
import domain.common.exception.NotExistsException;
import domain.sportObject.AbstractSportObjectTest;
import domain.sportObject.exception.SportObjectPositionNameAlreadyExists;
import org.junit.Test;
import query.model.embeddable.PositionsCount;

public final class UpdateSportObjectPositionTest extends AbstractSportObjectTest {

    private UpdateSportObjectPositionCommand command = UpdateSportObjectPositionCommand.builder()
            .sportObjectPositionId(sportObjectPositionId)
            .sportObjectId(sportObjectId)
            .name("name1")
            .description("description2")
            .positionsCount(new PositionsCount(12))
            .build();

    @Test
    public void shouldNotUpdateWhenNotExists() {
        testFixture.given(sportObjectCreatedEvent)
                .when(command)
                .expectNoEvents()
                .expectException(NotExistsException.class);
    }

    @Test
    public void shouldNotUpdateWhenIsAlreadyDeleted() {
        testFixture.given(sportObjectCreatedEvent, sportObjectPositionCreatedEvent, new SportObjectPositionDeletedEvent(sportObjectPositionId))
                .when(command)
                .expectNoEvents()
                .expectException(AlreadyDeletedException.class);
    }

    @Test
    public void shouldNotUpdateWhenNewNameAlreadyExists() {
        SportObjectPositionCreatedEvent otherPositionCreatedEvent = SportObjectPositionCreatedEvent.builder()
                .sportObjectPositionId(UUID.randomUUID())
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .name("name2")
                .description("description1")
                .positionsCount(new PositionsCount(11))
                .build();

        testFixture.given(sportObjectCreatedEvent, sportObjectPositionCreatedEvent, otherPositionCreatedEvent)
                .when(UpdateSportObjectPositionCommand.builder()
                        .sportObjectPositionId(sportObjectPositionId)
                        .sportObjectId(sportObjectId)
                        .name("name2")
                        .description("description2")
                        .positionsCount(new PositionsCount(12))
                        .build())
                .expectNoEvents()
                .expectException(SportObjectPositionNameAlreadyExists.class);
    }

    @Test
    public void shouldUpdateWhenExistsAndIsNotDeleted() {
        testFixture.given(sportObjectCreatedEvent, sportObjectPositionCreatedEvent)
                .when(command)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    SportObjectPositionUpdatedEvent event = (SportObjectPositionUpdatedEvent) p.getPayload();
                    return event.getSportObjectPositionId().equals(sportObjectPositionId);
                }), andNoMore()));
    }
}
