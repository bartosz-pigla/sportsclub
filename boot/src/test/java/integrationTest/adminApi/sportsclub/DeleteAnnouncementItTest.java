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

public final class DeleteAnnouncementItTest extends AbstractSportsclubItTest {

    @Autowired
    private AnnouncementEntityRepository announcementRepository;

    @Test
    @DirtiesContext
    public void shouldDeleteWhenSportsclubAndAnnouncementExists() {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateAnnouncementCommand createAnnouncementCommand = createAnnouncement(createSportsclubCommand);
        UUID announcementId = newArrayList(announcementRepository.findAll()).get(0).getId();
        signIn("superuser", "password");

        ResponseEntity<AnnouncementDto> deleteAnnouncementResponse = delete(
                ADMIN_CONSOLE_SPORTSCLUB_ANNOUNCEMENT_BY_ID,
                AnnouncementDto.class,
                createSportsclubCommand.getName(), announcementId.toString());

        assertEquals(deleteAnnouncementResponse.getStatusCode(), HttpStatus.OK);

        AnnouncementDto response = deleteAnnouncementResponse.getBody();
        assertEquals(createAnnouncementCommand.getTitle(), response.getTitle());
        assertEquals(createAnnouncementCommand.getContent(), response.getContent());
        assertTrue(announcementRepository.findById(announcementId).get().isDeleted());
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportsclubNotExists() {
        signIn("superuser", "password");

        ResponseEntity<AnnouncementDto> deleteAnnouncementResponse = delete(
                ADMIN_CONSOLE_SPORTSCLUB_ANNOUNCEMENT_BY_ID,
                AnnouncementDto.class,
                "notExistingSportsclubName", "notExistingAnnouncementId");

        assertEquals(deleteAnnouncementResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenAnnouncementNotExists() {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        signIn("superuser", "password");

        ResponseEntity<AnnouncementDto> deleteAnnouncementResponse = delete(
                ADMIN_CONSOLE_SPORTSCLUB_ANNOUNCEMENT_BY_ID,
                AnnouncementDto.class,
                createSportsclubCommand.getName(), "notExistingAnnouncementId");

        assertEquals(deleteAnnouncementResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
