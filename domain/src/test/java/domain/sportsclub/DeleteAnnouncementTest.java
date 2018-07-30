package domain.sportsclub;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.time.LocalDateTime;
import java.util.UUID;

import api.sportsclub.command.DeleteAnnouncementCommand;
import api.sportsclub.event.AnnouncementCreatedEvent;
import api.sportsclub.event.AnnouncementDeletedEvent;
import api.sportsclub.event.AnnouncementUpdatedEvent;
import api.sportsclub.event.SportsclubCreatedEvent;
import domain.common.exception.NotExistsException;
import org.junit.Test;

public final class DeleteAnnouncementTest extends SportsclubTest {

    private static SportsclubCreatedEvent sportsclubCreatedEvent = SportsclubCreatedEvent.builder()
            .sportsclubId(UUID.randomUUID())
            .name("name1")
            .description("description1")
            .build();

    private static AnnouncementCreatedEvent announcementCreatedEvent = AnnouncementCreatedEvent.builder()
            .announcementId(UUID.randomUUID())
            .title("title1")
            .content("content1")
            .createdOn(LocalDateTime.now())
            .build();

    @Test
    public void shouldNotDeleteWhenNotExists() {
        DeleteAnnouncementCommand deleteAnnouncementCommand =
                new DeleteAnnouncementCommand(sportsclubCreatedEvent.getSportsclubId(), UUID.randomUUID());

        testFixture.given(sportsclubCreatedEvent)
                .when(deleteAnnouncementCommand)
                .expectNoEvents()
                .expectException(NotExistsException.class);
    }

    @Test
    public void shouldDeleteCreated() {
        DeleteAnnouncementCommand deleteAnnouncementCommand =
                new DeleteAnnouncementCommand(sportsclubCreatedEvent.getSportsclubId(), announcementCreatedEvent.getAnnouncementId());

        testFixture.given(sportsclubCreatedEvent, announcementCreatedEvent)
                .when(deleteAnnouncementCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    AnnouncementDeletedEvent event = (AnnouncementDeletedEvent) p.getPayload();
                    return event.getAnnouncementId().equals(announcementCreatedEvent.getAnnouncementId());
                }), andNoMore()));
    }

    @Test
    public void shouldDeleteUpdated() {
        AnnouncementUpdatedEvent announcementUpdatedEvent = AnnouncementUpdatedEvent.builder()
                .announcementId(announcementCreatedEvent.getAnnouncementId())
                .title("title2")
                .content("content2")
                .build();
        DeleteAnnouncementCommand deleteAnnouncementCommand =
                new DeleteAnnouncementCommand(sportsclubCreatedEvent.getSportsclubId(), announcementUpdatedEvent.getAnnouncementId());

        testFixture.given(sportsclubCreatedEvent, announcementCreatedEvent, announcementUpdatedEvent)
                .when(deleteAnnouncementCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    AnnouncementDeletedEvent event = (AnnouncementDeletedEvent) p.getPayload();
                    return event.getAnnouncementId().equals(announcementUpdatedEvent.getAnnouncementId());
                }), andNoMore()));
    }
}
