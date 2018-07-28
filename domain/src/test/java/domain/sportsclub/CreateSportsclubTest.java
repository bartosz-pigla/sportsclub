package domain.sportsclub;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;
import static org.mockito.Mockito.when;

import java.util.UUID;

import api.sportsclub.command.CreateSportsclubCommand;
import api.sportsclub.event.SportsclubCreatedEvent;
import domain.common.exception.AlreadyCreatedException;
import org.junit.Test;

public final class CreateSportsclubTest extends SportsclubTest {

    private static SportsclubCreatedEvent createdEvent = SportsclubCreatedEvent.builder()
            .sportsclubId(UUID.randomUUID())
            .name("name1")
            .description("description1")
            .build();
    private static CreateSportsclubCommand createCommand = CreateSportsclubCommand.builder()
            .name("name1")
            .description("description1")
            .build();

    @Test
    public void shouldNotCreateWhenNameAlreadyExists() {
        when(sportsclubRepository.existsByName(createCommand.getName())).thenReturn(true);

        testFixture.given(createdEvent)
                .when(createCommand)
                .expectNoEvents()
                .expectException(AlreadyCreatedException.class);
    }

    @Test
    public void shouldCreateWhenHasUniqueName() {
        testFixture.givenNoPriorActivity()
                .when(createCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    SportsclubCreatedEvent event = (SportsclubCreatedEvent) p.getPayload();
                    return event.getName().equals("name1");
                }), andNoMore()));
    }
}