package integrationTest.adminApi.sportObject.sportObjectPosition;

import static org.junit.Assert.assertEquals;
import static web.common.RequestMappings.DIRECTOR_API_SPORT_OBJECT_POSITION;

import java.util.List;
import java.util.UUID;

import commons.ErrorCode;
import integrationTest.adminApi.sportObject.AbstractSportObjectItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import web.adminApi.sportObject.dto.SportObjectPositionDto;

public final class CreateSportObjectPositionItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldCreate() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        signIn("superuser", "password");

        SportObjectPositionDto position = SportObjectPositionDto.builder()
                .name("name1")
                .description("description1")
                .positionsCount(12)
                .build();

        ResponseEntity<SportObjectPositionDto> sportObjectPositionDtoResponseEntity = restTemplate.postForEntity(
                DIRECTOR_API_SPORT_OBJECT_POSITION,
                position,
                SportObjectPositionDto.class,
                sportsclubId.toString(),
                sportObjectId.toString());

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.OK);

        SportObjectPositionDto responseBody = sportObjectPositionDtoResponseEntity.getBody();
        assertEquals(position.getName(), responseBody.getName());
        assertEquals(position.getDescription(), responseBody.getDescription());
        assertEquals(position.getPositionsCount(), responseBody.getPositionsCount());
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenNameAlreadyExists() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        createSportObjectPosition(sportObjectId);
        signIn("superuser", "password");

        SportObjectPositionDto position = SportObjectPositionDto.builder()
                .name("name1")
                .description("description1")
                .positionsCount(12)
                .build();

        ResponseEntity<List> sportObjectPositionDtoResponseEntity = restTemplate.postForEntity(
                DIRECTOR_API_SPORT_OBJECT_POSITION,
                position,
                List.class,
                sportsclubId.toString(),
                sportObjectId.toString());

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.CONFLICT);

        List errors = sportObjectPositionDtoResponseEntity.getBody();
        assertField("name", ErrorCode.ALREADY_EXISTS.getCode(), errors);
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenNameAndPositionsCountIsEmpty() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        signIn("superuser", "password");

        ResponseEntity<List> sportObjectPositionDtoResponseEntity = restTemplate.postForEntity(
                DIRECTOR_API_SPORT_OBJECT_POSITION,
                new SportObjectPositionDto(),
                List.class,
                sportsclubId.toString(),
                sportObjectId.toString());

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        List errors = sportObjectPositionDtoResponseEntity.getBody();
        assertField("name", ErrorCode.EMPTY.getCode(), errors);
        assertField("positionsCount", ErrorCode.EMPTY.getCode(), errors);
    }
}
