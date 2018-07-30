package domain.sportsclub;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.time.LocalDateTime;
import java.util.UUID;

import api.sportsclub.command.UpdateAnnouncementCommand;
import api.sportsclub.event.AnnouncementCreatedEvent;
import api.sportsclub.event.AnnouncementUpdatedEvent;
import api.sportsclub.event.SportsclubCreatedEvent;
import domain.common.exception.NotExistsException;
import org.junit.Test;

public final class UpdateAnnouncementTest extends SportsclubTest {

    private static SportsclubCreatedEvent createdEvent = SportsclubCreatedEvent.builder()
            .sportsclubId(UUID.randomUUID())
            .name("name1")
            .description("description1")
            .build();

    @Test
    public void shouldNotUpdateWhenNotExists() {
        UpdateAnnouncementCommand updateAnnouncementCommand = UpdateAnnouncementCommand.builder()
                .announcementId(UUID.randomUUID())
                .sportsclubId(createdEvent.getSportsclubId())
                .title("title1")
                .content("content1")
                .build();

        testFixture.given(createdEvent)
                .when(updateAnnouncementCommand)
                .expectNoEvents()
                .expectException(NotExistsException.class);
    }

    @Test
    public void shouldUpdateWhenExists() {
        AnnouncementCreatedEvent announcementCreatedEvent = AnnouncementCreatedEvent.builder()
                .announcementId(UUID.randomUUID())
                .title("title1")
                .content("content1")
                .createdOn(LocalDateTime.now())
                .build();
        UpdateAnnouncementCommand updateAnnouncementCommand = UpdateAnnouncementCommand.builder()
                .announcementId(announcementCreatedEvent.getAnnouncementId())
                .sportsclubId(createdEvent.getSportsclubId())
                .title("title2")
                .content("content2")
                .build();

        testFixture.given(createdEvent, announcementCreatedEvent)
                .when(updateAnnouncementCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    AnnouncementUpdatedEvent event = (AnnouncementUpdatedEvent) p.getPayload();
                    return event.getTitle().equals(updateAnnouncementCommand.getTitle()) &&
                            event.getCreatedOn().isAfter(announcementCreatedEvent.getCreatedOn());
                }), andNoMore()));
    }
}
