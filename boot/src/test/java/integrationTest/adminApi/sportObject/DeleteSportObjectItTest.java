package integrationTest.adminApi.sportObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static query.model.sportobject.repository.SportObjectQueryExpressions.idMatches;
import static web.common.RequestMappings.DIRECTOR_API_SPORT_OBJECT_BY_ID;

import java.util.UUID;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import web.sportObject.dto.SportObjectDto;

public final class DeleteSportObjectItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldDeleteWhenSportsclubAndSportObjectExistsAndIsNotDeleted() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        signIn("superuser", "password");

        ResponseEntity<SportObjectDto> deleteSportObjectResponse = delete(
                DIRECTOR_API_SPORT_OBJECT_BY_ID,
                SportObjectDto.class,
                sportsclubId.toString(),
                sportObjectId.toString());

        assertEquals(deleteSportObjectResponse.getStatusCode(), HttpStatus.NO_CONTENT);
        assertFalse(sportObjectRepository.exists(idMatches(sportObjectId)));
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenSportsclubNotExists() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        signIn("superuser", "password");

        ResponseEntity<String> deleteSportObjectResponse = delete(
                DIRECTOR_API_SPORT_OBJECT_BY_ID,
                String.class,
                "notExistingSportsclubId",
                sportObjectId);

        assertEquals(deleteSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenSportObjectNotExists() {
        UUID sportsclubId = createSportsclub();
        createSportObject(sportsclubId);
        signIn("superuser", "password");

        ResponseEntity<String> deleteSportObjectResponse = delete(
                DIRECTOR_API_SPORT_OBJECT_BY_ID,
                String.class,
                sportsclubId.toString(),
                "notExistingSportObjectId");

        assertEquals(deleteSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);

    }
}
