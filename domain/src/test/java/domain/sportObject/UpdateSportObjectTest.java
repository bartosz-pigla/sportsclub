package domain.sportObject;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;
import static org.mockito.Mockito.when;
import static query.model.baseEntity.repository.BaseEntityQueryExpressions.idMatches;
import static query.model.sportobject.repository.SportObjectQueryExpressions.nameAndIdMatches;

import java.util.UUID;

import api.sportObject.command.UpdateSportObjectCommand;
import api.sportObject.event.SportObjectDeletedEvent;
import api.sportObject.event.SportObjectUpdatedEvent;
import domain.common.exception.AlreadyCreatedException;
import domain.common.exception.AlreadyDeletedException;
import domain.sportObject.exception.NotAssignedToAnySportsclubException;
import org.junit.Test;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;

public final class UpdateSportObjectTest extends AbstractSportObjectTest {

    @Test
    public void shouldNotUpdateWhenIsAlreadyDeleted() {
        UUID sportObjectId = sportObjectCreatedEvent.getSportObjectId();

        SportObjectDeletedEvent sportObjectDeletedEvent = SportObjectDeletedEvent.builder()
                .sportObjectId(sportObjectId)
                .build();

        UpdateSportObjectCommand updateSportObjectCommand = UpdateSportObjectCommand.builder()
                .sportObjectId(sportObjectId)
                .sportsclubId(sportsclubCreatedEvent.getSportsclubId())
                .address(new Address("street2", new City("WroclawTwo"), new Coordinates(0d, 0d)))
                .description("description2")
                .name("name2")
                .build();

        testFixture.given(sportObjectCreatedEvent, sportObjectDeletedEvent)
                .when(updateSportObjectCommand)
                .expectException(AlreadyDeletedException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldNotUpdateWhenNameAlreadyExists() {
        UUID sportObjectId = sportObjectCreatedEvent.getSportObjectId();

        UpdateSportObjectCommand updateSportObjectCommand = UpdateSportObjectCommand.builder()
                .sportObjectId(sportObjectId)
                .sportsclubId(sportsclubCreatedEvent.getSportsclubId())
                .address(new Address("street2", new City("WroclawTwo"), new Coordinates(0d, 0d)))
                .description("description2")
                .name("name2")
                .build();

        when(sportObjectRepository.exists(nameAndIdMatches(updateSportObjectCommand.getName(), sportObjectId))).thenReturn(true);

        testFixture.given(sportObjectCreatedEvent)
                .when(updateSportObjectCommand)
                .expectException(AlreadyCreatedException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldNotUpdateWhenIsNotAssignedToAnySportsclub() {
        UUID sportObjectId = sportObjectCreatedEvent.getSportObjectId();

        UpdateSportObjectCommand updateSportObjectCommand = UpdateSportObjectCommand.builder()
                .sportObjectId(sportObjectId)
                .address(new Address("street2", new City("WroclawTwo"), new Coordinates(0d, 0d)))
                .description("description2")
                .name("name2")
                .build();

        when(sportObjectRepository.exists(nameAndIdMatches(updateSportObjectCommand.getName(), sportObjectId))).thenReturn(false);
        when(sportsclubRepository.exists(idMatches(updateSportObjectCommand.getSportsclubId()))).thenReturn(false);

        testFixture.given(sportObjectCreatedEvent)
                .when(updateSportObjectCommand)
                .expectException(NotAssignedToAnySportsclubException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldUpdateWhenIsAssignedToSportsclub() {
        UUID sportObjectId = sportObjectCreatedEvent.getSportObjectId();

        UpdateSportObjectCommand updateSportObjectCommand = UpdateSportObjectCommand.builder()
                .sportObjectId(sportObjectId)
                .sportsclubId(sportsclubCreatedEvent.getSportsclubId())
                .address(new Address("street2", new City("WroclawTwo"), new Coordinates(0d, 0d)))
                .description("description2")
                .name("name2")
                .build();

        when(sportObjectRepository.exists(nameAndIdMatches(updateSportObjectCommand.getName(), sportObjectId))).thenReturn(false);
        when(sportsclubRepository.exists(idMatches(updateSportObjectCommand.getSportsclubId()))).thenReturn(true);

        testFixture.given(sportObjectCreatedEvent)
                .when(updateSportObjectCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    SportObjectUpdatedEvent event = (SportObjectUpdatedEvent) p.getPayload();
                    return event.getSportObjectId().equals(updateSportObjectCommand.getSportObjectId());
                }), andNoMore()));
    }
}
