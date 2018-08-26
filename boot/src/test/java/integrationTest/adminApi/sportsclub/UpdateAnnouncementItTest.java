package integrationTest.adminApi.sportsclub;

import static com.google.common.collect.Lists.newArrayList;
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
import web.adminApi.announcement.dto.AnnouncementDto;

public final class UpdateAnnouncementItTest extends AbstractSportsclubItTest {

    @Autowired
    private AnnouncementEntityRepository announcementRepository;

    @Test
    @DirtiesContext
    public void shouldUpdateWhenSportsclubAndAnnouncementExists() {
        UUID sportsclubId = createSportsclub();
        UUID announcementId = createAnnouncement(sportsclubId);
        signIn("superuser", "password");

        AnnouncementDto announcement = AnnouncementDto.builder()
                .title("title2")
                .content("content2")
                .build();

        ResponseEntity<AnnouncementDto> updateAnnouncementResponse = put(
                DIRECTOR_API_SPORTSCLUB_ANNOUNCEMENT_BY_ID,
                announcement,
                AnnouncementDto.class,
                sportsclubId.toString(),
                announcementId.toString());

        assertEquals(updateAnnouncementResponse.getStatusCode(), HttpStatus.NO_CONTENT);

        assertTrue(newArrayList(announcementRepository.findAll()).stream()
                .anyMatch(a -> a.getTitle().equals(announcement.getTitle()) && a.getContent().equals(announcement.getContent())));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportsclubNotExists() {
        UUID sportsclubId = createSportsclub();
        UUID announcementId = createAnnouncement(sportsclubId);
        signIn("superuser", "password");

        AnnouncementDto announcement = AnnouncementDto.builder()
                .title("title2")
                .content("content2")
                .build();

        ResponseEntity<String> updateAnnouncementResponse = put(
                DIRECTOR_API_SPORTSCLUB_ANNOUNCEMENT_BY_ID,
                announcement,
                String.class,
                "notExistingSportsclubId",
                announcementId.toString());

        assertEquals(updateAnnouncementResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenAnnouncementNotExists() {
        UUID sportsclubId = createSportsclub();
        signIn("superuser", "password");

        AnnouncementDto announcement = AnnouncementDto.builder()
                .title("title2")
                .content("content2")
                .build();

        ResponseEntity<String> updateAnnouncementResponse = put(
                DIRECTOR_API_SPORTSCLUB_ANNOUNCEMENT_BY_ID,
                announcement,
                String.class,
                sportsclubId.toString(),
                "notExistingAnnouncementId");

        assertEquals(updateAnnouncementResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
