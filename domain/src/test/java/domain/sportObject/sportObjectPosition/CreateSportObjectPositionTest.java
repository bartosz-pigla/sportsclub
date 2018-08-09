package domain.sportObject.sportObjectPosition;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import api.sportObject.sportObjectPosition.command.CreateSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.event.SportObjectPositionCreatedEvent;
import domain.sportObject.AbstractSportObjectTest;
import domain.sportObject.exception.SportObjectPositionNameAlreadyExists;
import org.junit.Test;
import query.model.embeddable.PositiveNumber;

public final class CreateSportObjectPositionTest extends AbstractSportObjectTest {

    @Test
    public void shouldNotCreateWhenNameAlreadyExists() {
        CreateSportObjectPositionCommand command = CreateSportObjectPositionCommand.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .name(sportObjectPositionCreatedEvent.getName())
                .description("description1")
                .positionsCount(new PositiveNumber(12))
                .build();

        testFixture.given(sportObjectCreatedEvent, sportObjectPositionCreatedEvent)
                .when(command)
                .expectNoEvents()
                .expectException(SportObjectPositionNameAlreadyExists.class);
    }

    @Test
    public void shouldCreate() {
        CreateSportObjectPositionCommand command = CreateSportObjectPositionCommand.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .name("name2")
                .description("description1")
                .positionsCount(new PositiveNumber(12))
                .build();

        testFixture.given(sportObjectCreatedEvent, sportObjectPositionCreatedEvent)
                .when(command)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    SportObjectPositionCreatedEvent event = (SportObjectPositionCreatedEvent) p.getPayload();
                    return event.getName().equals(command.getName());
                }), andNoMore()));
    }
}
