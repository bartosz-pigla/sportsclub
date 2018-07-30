package domain.sportsclub;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.util.UUID;

import api.sportsclub.command.CreateAnnouncementCommand;
import api.sportsclub.event.AnnouncementCreatedEvent;
import api.sportsclub.event.SportsclubCreatedEvent;
import org.junit.Test;

public final class CreateAnnouncementTest extends SportsclubTest {

    private static SportsclubCreatedEvent createdEvent = SportsclubCreatedEvent.builder()
            .sportsclubId(UUID.randomUUID())
            .name("name1")
            .description("description1")
            .build();

    @Test
    public void shouldCreateAnnouncement() {
        CreateAnnouncementCommand createAnnouncementCommand = CreateAnnouncementCommand.builder()
                .sportsclubId(createdEvent.getSportsclubId())
                .title("title1")
                .content("content1")
                .build();

        testFixture.given(createdEvent)
                .when(createAnnouncementCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    AnnouncementCreatedEvent event = (AnnouncementCreatedEvent) p.getPayload();
                    return event.getTitle().equals(createAnnouncementCommand.getTitle());
                }), andNoMore()));
    }
}
