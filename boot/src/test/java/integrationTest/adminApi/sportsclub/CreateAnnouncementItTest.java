package integrationTest.adminApi.sportsclub;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static web.common.RequestMappings.DIRECTOR_API_ANNOUNCEMENT;

import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.announcement.repository.AnnouncementEntityRepository;
import web.adminApi.announcement.dto.AnnouncementDto;

public final class CreateAnnouncementItTest extends AbstractSportsclubItTest {

    @Autowired
    private AnnouncementEntityRepository announcementRepository;

    @Test
    @DirtiesContext
    public void shouldCreateWhenSportsclubExists() {
        UUID sportsclubId = createSportsclub();
        signIn("superuser", "password");

        AnnouncementDto announcement = AnnouncementDto.builder()
                .title("title1")
                .content("content1")
                .build();

        ResponseEntity<AnnouncementDto> createAnnouncementResponse = restTemplate.postForEntity(
                DIRECTOR_API_ANNOUNCEMENT,
                announcement,
                AnnouncementDto.class,
                sportsclubId.toString());

        assertEquals(createAnnouncementResponse.getStatusCode(), HttpStatus.OK);

        AnnouncementDto response = createAnnouncementResponse.getBody();
        assertEquals(announcement.getTitle(), response.getTitle());
        assertEquals(announcement.getContent(), response.getContent());

        assertTrue(newArrayList(announcementRepository.findAll()).stream().anyMatch(a ->
                a.getTitle().equals(announcement.getTitle()) && a.getContent().equals(announcement.getContent())));
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenSportsclubNotExists() {
        signIn("superuser", "password");

        AnnouncementDto announcement = AnnouncementDto.builder()
                .title("title1")
                .content("content1")
                .build();

        ResponseEntity<String> createAnnouncementResponse = restTemplate.postForEntity(
                DIRECTOR_API_ANNOUNCEMENT,
                announcement,
                String.class,
                "notExistingSportsclub");

        assertEquals(createAnnouncementResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
