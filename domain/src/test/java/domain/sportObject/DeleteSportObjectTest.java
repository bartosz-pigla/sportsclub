package domain.sportObject;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import api.sportObject.command.DeleteSportObjectCommand;
import api.sportObject.event.SportObjectDeletedEvent;
import domain.common.exception.AlreadyDeletedException;
import org.junit.Test;

public final class DeleteSportObjectTest extends AbstractSportObjectTest {

    private DeleteSportObjectCommand deleteSportObjectCommand = DeleteSportObjectCommand.builder()
            .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
            .build();

    @Test
    public void shouldNotDeleteWhenIsAlreadyDeleted() {
        SportObjectDeletedEvent sportObjectDeletedEvent = SportObjectDeletedEvent.builder()
                .sportObjectId(sportObjectCreatedEvent.getSportObjectId())
                .build();

        testFixture.given(sportObjectCreatedEvent, sportObjectDeletedEvent)
                .when(deleteSportObjectCommand)
                .expectException(AlreadyDeletedException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldDeleteWhenIsNotDeleted() {
        testFixture.given(sportObjectCreatedEvent)
                .when(deleteSportObjectCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    SportObjectDeletedEvent event = (SportObjectDeletedEvent) p.getPayload();
                    return event.getSportObjectId().equals(sportObjectCreatedEvent.getSportObjectId());
                }), andNoMore()));
    }
}
