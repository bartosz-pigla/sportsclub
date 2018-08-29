package integrationTest.adminApi.sportsclub;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static web.common.RequestMappings.DIRECTOR_API_SPORTSCLUB_ANNOUNCEMENT_BY_ID;

import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.announcement.repository.AnnouncementEntityRepository;
import web.announcement.dto.AnnouncementDto;

public final class DeleteAnnouncementItTest extends AbstractSportsclubItTest {

    @Autowired
    private AnnouncementEntityRepository announcementRepository;

    @Test
    @DirtiesContext
    public void shouldDeleteWhenSportsclubAndAnnouncementExists() {
        UUID sportsclubId = createSportsclub();
        UUID announcementId = createAnnouncement(sportsclubId);
        signIn("superuser", "password");

        ResponseEntity<String> deleteAnnouncementResponse = delete(
                DIRECTOR_API_SPORTSCLUB_ANNOUNCEMENT_BY_ID,
                String.class,
                sportsclubId.toString(),
                announcementId.toString());

        assertEquals(deleteAnnouncementResponse.getStatusCode(), HttpStatus.NO_CONTENT);
        assertTrue(announcementRepository.findById(announcementId).get().isDeleted());
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportsclubNotExists() {
        signIn("superuser", "password");

        ResponseEntity<AnnouncementDto> deleteAnnouncementResponse = delete(
                DIRECTOR_API_SPORTSCLUB_ANNOUNCEMENT_BY_ID,
                AnnouncementDto.class,
                "notExistingSportsclubID",
                "notExistingAnnouncementId");

        assertEquals(deleteAnnouncementResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenAnnouncementNotExists() {
        UUID sportsclubId = createSportsclub();
        signIn("superuser", "password");

        ResponseEntity<AnnouncementDto> deleteAnnouncementResponse = delete(
                DIRECTOR_API_SPORTSCLUB_ANNOUNCEMENT_BY_ID,
                AnnouncementDto.class,
                sportsclubId.toString(),
                "notExistingAnnouncementId");

        assertEquals(deleteAnnouncementResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
