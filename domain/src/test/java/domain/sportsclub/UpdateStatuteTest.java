package domain.sportsclub;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.util.UUID;

import api.sportsclub.command.UpdateStatuteCommand;
import api.sportsclub.event.SportsclubCreatedEvent;
import api.sportsclub.event.StatuteAddedEvent;
import api.sportsclub.event.StatuteUpdatedEvent;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.junit.Test;

public final class UpdateStatuteTest extends SportsclubTest {

    private static SportsclubCreatedEvent sportsclubCreatedEvent = SportsclubCreatedEvent.builder()
            .sportsclubId(UUID.randomUUID())
            .name("name1")
            .description("description1")
            .build();

    @Test
    public void shouldNotUpdateWhenSportsclubNotExists() {
        UpdateStatuteCommand updateStatuteCommand = UpdateStatuteCommand.builder()
                .title("title")
                .description("description")
                .sportsclubId(UUID.randomUUID())
                .build();

        testFixture.givenNoPriorActivity()
                .when(updateStatuteCommand)
                .expectNoEvents()
                .expectException(AggregateNotFoundException.class);
    }

    @Test
    public void shouldUpdateWhenSportsclubAndStatuteAlreadyExists() {
        StatuteAddedEvent statuteAddedEvent = StatuteAddedEvent.builder()
                .title("title1")
                .description("description1")
                .statuteId(UUID.randomUUID())
                .sportsclubId(sportsclubCreatedEvent.getSportsclubId())
                .build();

        UpdateStatuteCommand updateStatuteCommand = UpdateStatuteCommand.builder()
                .title("title2")
                .description("description")
                .sportsclubId(sportsclubCreatedEvent.getSportsclubId()).build();

        testFixture.given(sportsclubCreatedEvent, statuteAddedEvent)
                .when(updateStatuteCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    StatuteUpdatedEvent event = (StatuteUpdatedEvent) p.getPayload();
                    return event.getStatuteId().equals(statuteAddedEvent.getStatuteId());
                }), andNoMore()));
    }

    @Test
    public void shouldAddWhenSportsclubExists() {
        UpdateStatuteCommand updateStatuteCommand = UpdateStatuteCommand.builder()
                .title("title")
                .description("description")
                .sportsclubId(sportsclubCreatedEvent.getSportsclubId())
                .build();

        testFixture.given(sportsclubCreatedEvent)
                .when(updateStatuteCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    StatuteAddedEvent event = (StatuteAddedEvent) p.getPayload();
                    return event.getSportsclubId().equals(updateStatuteCommand.getSportsclubId()) &&
                            event.getTitle().equals(updateStatuteCommand.getTitle());
                }), andNoMore()));
    }
}
