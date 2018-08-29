package integrationTest.adminApi.sportObject.openingTime;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.DIRECTOR_API_OPENING_TIME_BY_ID;

import java.util.List;
import java.util.UUID;

import api.sportObject.openingTime.command.DeleteOpeningTimeCommand;
import integrationTest.adminApi.sportObject.AbstractSportObjectItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.sportobject.repository.OpeningTimeQueryExpressions;
import web.sportObject.dto.OpeningTimeDto;

public final class DeleteOpeningTimeItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldDeleteWhenExistsAndNotDeleted() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        UUID openingTimeId = createOpeningTime(sportObjectId);
        signIn("superuser", "password");

        ResponseEntity<OpeningTimeDto> deleteOpeningTimeRangeDtoResponseEntity = delete(
                DIRECTOR_API_OPENING_TIME_BY_ID,
                OpeningTimeDto.class,
                sportsclubId.toString(),
                sportObjectId.toString(),
                openingTimeId.toString());

        assertEquals(deleteOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
        assertTrue(openingTimeRepository.findById(openingTimeId).get().isDeleted());
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenNotExists() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        UUID openingTimeId = createOpeningTime(sportObjectId);
        signIn("superuser", "password");

        ResponseEntity<Object> deleteOpeningTimeRangeDtoResponseEntity = delete(
                DIRECTOR_API_OPENING_TIME_BY_ID,
                Object.class,
                sportsclubId.toString(),
                sportObjectId.toString(),
                "notExistingOpeningTimeId");

        assertEquals(deleteOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        assertTrue(openingTimeRepository.findOne(
                OpeningTimeQueryExpressions.idMatches(openingTimeId)).isPresent());
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenIsAlreadyDeleted() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        UUID openingTimeId = createOpeningTime(sportObjectId);
        signIn("superuser", "password");

        commandGateway.sendAndWait(DeleteOpeningTimeCommand.builder()
                .sportObjectId(sportObjectId)
                .openingTimeId(openingTimeId).build());

        ResponseEntity<List> deleteOpeningTimeRangeDtoResponseEntity = delete(
                DIRECTOR_API_OPENING_TIME_BY_ID,
                List.class,
                sportsclubId.toString(),
                sportObjectId.toString(),
                openingTimeId.toString());

        assertEquals(deleteOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        assertFalse(openingTimeRepository.findOne(
                OpeningTimeQueryExpressions.idMatches(openingTimeId)).isPresent());
    }
}
