package integrationTest.adminApi.sportsclub;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static web.common.RequestMappings.ADMIN_CONSOLE_SPORTSCLUB_ANNOUNCEMENT_BY_ID;

import java.util.UUID;

import api.sportsclub.command.CreateAnnouncementCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.repository.AnnouncementEntityRepository;
import web.adminApi.announcement.dto.AnnouncementDto;

public final class UpdateAnnouncementItTest extends AbstractSportsclubItTest {

    @Autowired
    private AnnouncementEntityRepository announcementRepository;

    @Test
    @DirtiesContext
    public void shouldUpdateWhenSportsclubAndAnnouncementExists() {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateAnnouncementCommand createAnnouncementCommand = createAnnouncement(createSportsclubCommand);
        UUID announcementId = newArrayList(announcementRepository.findAll()).get(0).getId();
        signIn("superuser", "password");
        AnnouncementDto announcement = AnnouncementDto.builder()
                .title("title2")
                .content("content2")
                .build();

        ResponseEntity<AnnouncementDto> updateAnnouncementResponse = put(
                ADMIN_CONSOLE_SPORTSCLUB_ANNOUNCEMENT_BY_ID,
                announcement,
                AnnouncementDto.class,
                createSportsclubCommand.getName(), announcementId);

        assertEquals(updateAnnouncementResponse.getStatusCode(), HttpStatus.OK);

        AnnouncementDto response = updateAnnouncementResponse.getBody();
        assertEquals(announcement.getTitle(), response.getTitle());
        assertEquals(announcement.getContent(), response.getContent());
        assertTrue(newArrayList(announcementRepository.findAll()).stream()
                .anyMatch(a -> a.getTitle().equals(announcement.getTitle()) && a.getContent().equals(announcement.getContent())));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportsclubNotExists() {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateAnnouncementCommand createAnnouncementCommand = createAnnouncement(createSportsclubCommand);
        UUID announcementId = newArrayList(announcementRepository.findAll()).get(0).getId();
        signIn("superuser", "password");
        AnnouncementDto announcement = AnnouncementDto.builder()
                .title("title2")
                .content("content2")
                .build();

        ResponseEntity<AnnouncementDto> updateAnnouncementResponse = put(
                ADMIN_CONSOLE_SPORTSCLUB_ANNOUNCEMENT_BY_ID,
                announcement,
                AnnouncementDto.class,
                "notExistingSportsclubName", announcementId);

        assertEquals(updateAnnouncementResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenAnnouncementNotExists() {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        signIn("superuser", "password");
        AnnouncementDto announcement = AnnouncementDto.builder()
                .title("title2")
                .content("content2")
                .build();

        ResponseEntity<AnnouncementDto> updateAnnouncementResponse = put(
                ADMIN_CONSOLE_SPORTSCLUB_ANNOUNCEMENT_BY_ID,
                announcement,
                AnnouncementDto.class,
                createSportsclubCommand.getName(), "notExistingAnnouncementId");

        assertEquals(updateAnnouncementResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
