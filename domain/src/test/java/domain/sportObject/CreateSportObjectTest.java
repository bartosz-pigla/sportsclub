package domain.sportObject;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.event.SportObjectCreatedEvent;
import domain.common.exception.AlreadyCreatedException;
import domain.sportObject.exception.NotAssignedToAnySportsclubException;
import org.junit.Test;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;

public final class CreateSportObjectTest extends AbstractSportObjectTest {

    @Test
    public void shouldNotCreateWhenIsNotAssignedToAnySportsclub() throws MalformedURLException {
        CreateSportObjectCommand createSportObjectCommand = CreateSportObjectCommand.builder()
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .description("description1")
                .image(new URL("https://www.w3schools.com/w3css/img_lights.jpg"))
                .name("name1").build();

        when(sportObjectRepository.existsByNameAndDeletedFalse(createSportObjectCommand.getName())).thenReturn(false);
        when(sportsclubRepository.existsById(createSportObjectCommand.getSportsclubId())).thenReturn(false);

        testFixture.given(sportsclubCreatedEvent)
                .when(createSportObjectCommand)
                .expectException(NotAssignedToAnySportsclubException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldNotCreateWhenIsAssignedToNotExistingSportsclub() throws MalformedURLException {
        CreateSportObjectCommand createSportObjectCommand = CreateSportObjectCommand.builder()
                .sportsclubId(UUID.randomUUID())
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .description("description1")
                .image(new URL("https://www.w3schools.com/w3css/img_lights.jpg"))
                .name("name1").build();

        when(sportObjectRepository.existsByNameAndDeletedFalse(createSportObjectCommand.getName())).thenReturn(false);
        when(sportsclubRepository.existsById(createSportObjectCommand.getSportsclubId())).thenReturn(false);

        testFixture.given(sportsclubCreatedEvent)
                .when(createSportObjectCommand)
                .expectException(NotAssignedToAnySportsclubException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldNotCreateWhenNameAlreadyExists() throws MalformedURLException {
        CreateSportObjectCommand createSportObjectCommand = CreateSportObjectCommand.builder()
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .description("description1")
                .image(new URL("https://www.w3schools.com/w3css/img_lights.jpg"))
                .name("name1").build();

        when(sportObjectRepository.existsByNameAndDeletedFalse(createSportObjectCommand.getName())).thenReturn(true);

        testFixture.given(sportsclubCreatedEvent)
                .when(createSportObjectCommand)
                .expectException(AlreadyCreatedException.class)
                .expectNoEvents();
    }

    @Test
    public void shouldCreateWhenIsAssignedToSportsclub() throws MalformedURLException {
        CreateSportObjectCommand createSportObjectCommand = CreateSportObjectCommand.builder()
                .sportsclubId(sportsclubCreatedEvent.getSportsclubId())
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .description("description1")
                .image(new URL("https://www.w3schools.com/w3css/img_lights.jpg"))
                .name("name1").build();

        when(sportObjectRepository.existsByNameAndDeletedFalse(createSportObjectCommand.getName())).thenReturn(false);
        when(sportsclubRepository.existsById(createSportObjectCommand.getSportsclubId())).thenReturn(true);

        testFixture.given(sportsclubCreatedEvent)
                .when(createSportObjectCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    SportObjectCreatedEvent event = (SportObjectCreatedEvent) p.getPayload();
                    return event.getSportsclubId().equals(sportsclubCreatedEvent.getSportsclubId()) && event.getName().equals(createSportObjectCommand.getName());
                }), andNoMore()));
    }
}
