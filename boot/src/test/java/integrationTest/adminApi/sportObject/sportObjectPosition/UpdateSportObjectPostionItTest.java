package integrationTest.adminApi.sportObject.sportObjectPosition;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID;

import java.util.List;
import java.util.UUID;

import api.sportObject.sportObjectPosition.command.CreateSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.command.DeleteSportObjectPositionCommand;
import commons.ErrorCode;
import integrationTest.adminApi.sportObject.AbstractSportObjectItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.embeddable.PositionsCount;
import query.model.sportobject.repository.SportObjectPositionQueryExpressions;
import web.adminApi.sportObject.dto.SportObjectPositionDto;

public final class UpdateSportObjectPostionItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldUpdateWhenExistsAndIsNotDeletedAndNameIsUnique() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        UUID sportObjectPositionId = createSportObjectPosition(sportObjectId);
        signIn("superuser", "password");

        SportObjectPositionDto position = SportObjectPositionDto.builder()
                .name("name2")
                .description("description2")
                .positionsCount(2)
                .build();

        ResponseEntity<SportObjectPositionDto> sportObjectPositionDtoResponseEntity = put(
                DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID,
                position,
                SportObjectPositionDto.class,
                sportsclubId.toString(),
                sportObjectId.toString(),
                sportObjectPositionId.toString());

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.NO_CONTENT);

        assertTrue(sportObjectPositionRepository.findOne(
                SportObjectPositionQueryExpressions.nameMatches(position.getName())).isPresent());
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenNameAlreadyExists() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        UUID sportObjectPositionId = createSportObjectPosition(sportObjectId);
        signIn("superuser", "password");

        commandGateway.sendAndWait(CreateSportObjectPositionCommand.builder()
                .sportObjectId(sportObjectId)
                .name("name2")
                .description("description2")
                .positionsCount(new PositionsCount(12))
                .build());

        SportObjectPositionDto position = SportObjectPositionDto.builder()
                .name("name2")
                .description("description2")
                .positionsCount(2)
                .build();

        ResponseEntity<List> sportObjectPositionDtoResponseEntity = put(
                DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID,
                position,
                List.class,
                sportsclubId.toString(),
                sportObjectId.toString(),
                sportObjectPositionId.toString());

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.CONFLICT);

        List errors = sportObjectPositionDtoResponseEntity.getBody();
        assertField("name", ErrorCode.ALREADY_EXISTS.getCode(), errors);
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenIsAlreadyDeleted() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        UUID sportObjectPositionId = createSportObjectPosition(sportObjectId);
        commandGateway.sendAndWait(new DeleteSportObjectPositionCommand(sportObjectId, sportObjectPositionId));
        signIn("superuser", "password");

        SportObjectPositionDto position = SportObjectPositionDto.builder()
                .name("name1")
                .description("description2")
                .positionsCount(2)
                .build();

        ResponseEntity<List> sportObjectPositionDtoResponseEntity = put(
                DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID,
                position,
                List.class,
                sportsclubId.toString(),
                sportObjectId.toString(),
                sportObjectPositionId.toString());

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        assertFalse(sportObjectPositionRepository.findOne(
                SportObjectPositionQueryExpressions.nameMatches(position.getName())).isPresent());
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenNotExists() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        signIn("superuser", "password");

        SportObjectPositionDto position = SportObjectPositionDto.builder()
                .name("name1")
                .description("description2")
                .positionsCount(2)
                .build();

        ResponseEntity<String> sportObjectPositionDtoResponseEntity = put(
                DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID,
                position,
                String.class,
                sportsclubId.toString(),
                sportObjectId.toString(),
                "notExistingSportObjectPositionName");

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        assertFalse(sportObjectPositionRepository.findOne(
                SportObjectPositionQueryExpressions.nameMatches(position.getName())).isPresent());
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenNameAndPositionsCountIsEmpty() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        UUID sportObjectPositionId = createSportObjectPosition(sportObjectId);
        signIn("superuser", "password");

        ResponseEntity<List> sportObjectPositionDtoResponseEntity = put(
                DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID,
                new SportObjectPositionDto(),
                List.class,
                sportsclubId.toString(),
                sportObjectId.toString(),
                sportObjectPositionId.toString());

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        List errors = sportObjectPositionDtoResponseEntity.getBody();
        assertField("name", ErrorCode.EMPTY.getCode(), errors);
        assertField("positionsCount", ErrorCode.EMPTY.getCode(), errors);

        assertTrue(sportObjectPositionRepository.findOne(
                SportObjectPositionQueryExpressions.idMatches(sportObjectPositionId)).get().getName().equals("name1"));
    }
}
