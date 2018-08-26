package integrationTest.adminApi.sportObject.sportObjectPosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID;

import java.util.UUID;

import api.sportObject.sportObjectPosition.command.DeleteSportObjectPositionCommand;
import integrationTest.adminApi.sportObject.AbstractSportObjectItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.sportobject.repository.SportObjectPositionQueryExpressions;

public final class DeleteSportObjectPositionItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldDeleteWhenIsNotDeletedAndExists() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        UUID sportObjectPositionId = createSportObjectPosition(sportObjectId);
        signIn("superuser", "password");

        ResponseEntity<String> deletePositionResponseEntity = delete(
                DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID,
                String.class,
                sportsclubId.toString(),
                sportObjectId.toString(),
                sportObjectPositionId.toString());

        assertEquals(deletePositionResponseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
        assertFalse(sportObjectPositionRepository.findOne(
                SportObjectPositionQueryExpressions.idMatches(sportObjectPositionId)).isPresent());
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenNotExists() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        signIn("superuser", "password");

        ResponseEntity<String> deletePositionResponseEntity = delete(
                DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID,
                String.class,
                sportsclubId.toString(),
                sportObjectId.toString(),
                "notExistingPositionId");

        assertEquals(deletePositionResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenIsAlreadyDeleted() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        UUID sportObjectPositionId = createSportObjectPosition(sportObjectId);

        commandGateway.sendAndWait(DeleteSportObjectPositionCommand.builder()
                .sportObjectId(sportObjectId)
                .sportObjectPositionId(sportObjectPositionId).build());

        signIn("superuser", "password");

        ResponseEntity<String> deletePositionResponseEntity = delete(
                DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID,
                String.class,
                sportsclubId.toString(),
                sportObjectId.toString(),
                sportObjectPositionId.toString());

        assertEquals(deletePositionResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        assertFalse(sportObjectPositionRepository.findOne(
                SportObjectPositionQueryExpressions.idMatches(sportObjectPositionId)).isPresent());
    }
}
