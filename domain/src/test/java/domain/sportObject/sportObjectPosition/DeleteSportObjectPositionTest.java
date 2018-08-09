package domain.sportObject.sportObjectPosition;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import api.sportObject.sportObjectPosition.command.DeleteSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.event.SportObjectPositionDeletedEvent;
import domain.common.exception.AlreadyDeletedException;
import domain.common.exception.NotExistsException;
import domain.sportObject.AbstractSportObjectTest;
import org.junit.Test;

public final class DeleteSportObjectPositionTest extends AbstractSportObjectTest {

    private DeleteSportObjectPositionCommand command = new DeleteSportObjectPositionCommand(sportObjectId, sportObjectPositionId);

    @Test
    public void shouldNotDeleteWhenIsAlreadyDeleted() {
        testFixture.given(sportObjectCreatedEvent, sportObjectPositionCreatedEvent, new SportObjectPositionDeletedEvent(sportObjectPositionId))
                .when(command)
                .expectNoEvents()
                .expectException(AlreadyDeletedException.class);
    }

    @Test
    public void shouldNotDeleteWhenNotExists() {
        testFixture.given(sportObjectCreatedEvent)
                .when(command)
                .expectNoEvents()
                .expectException(NotExistsException.class);
    }

    @Test
    public void shouldDeleteWhenExistsAndIsNotDeleted() {
        testFixture.given(sportObjectCreatedEvent, sportObjectPositionCreatedEvent)
                .when(command)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    SportObjectPositionDeletedEvent event = (SportObjectPositionDeletedEvent) p.getPayload();
                    return event.getSportObjectPositionId().equals(sportObjectPositionId);
                }), andNoMore()));
    }
}
