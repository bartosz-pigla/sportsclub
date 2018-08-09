package integrationTest.adminApi.sportObject.sportObjectPosition;

import static org.junit.Assert.assertEquals;
import static web.common.RequestMappings.ADMIN_CONSOLE_SPORT_OBJECT_POSITION;

import java.net.MalformedURLException;
import java.util.List;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportsclub.command.CreateSportsclubCommand;
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
    public void shouldCreate() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        SportObjectPositionDto position = SportObjectPositionDto.builder()
                .name("name1")
                .description("description1")
                .positionsCount(12)
                .build();

        ResponseEntity<SportObjectPositionDto> sportObjectPositionDtoResponseEntity = restTemplate.postForEntity(
                ADMIN_CONSOLE_SPORT_OBJECT_POSITION,
                position,
                SportObjectPositionDto.class,
                sportsclubName, sportObjectName);

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.OK);

        SportObjectPositionDto responseBody = sportObjectPositionDtoResponseEntity.getBody();
        assertEquals(position.getName(), responseBody.getName());
        assertEquals(position.getDescription(), responseBody.getDescription());
        assertEquals(position.getPositionsCount(), responseBody.getPositionsCount());
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenNameAlreadyExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        SportObjectPositionDto position = new SportObjectPositionDto();

        ResponseEntity<List> sportObjectPositionDtoResponseEntity = restTemplate.postForEntity(
                ADMIN_CONSOLE_SPORT_OBJECT_POSITION,
                position,
                List.class,
                sportsclubName, sportObjectName);

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.CONFLICT);

        List errors = sportObjectPositionDtoResponseEntity.getBody();
        assertField("name", ErrorCode.ALREADY_EXISTS.getCode(), errors);
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenNameAndPositionsCountIsEmpty() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        SportObjectPositionDto position = new SportObjectPositionDto();

        ResponseEntity<List> sportObjectPositionDtoResponseEntity = restTemplate.postForEntity(
                ADMIN_CONSOLE_SPORT_OBJECT_POSITION,
                position,
                List.class,
                sportsclubName, sportObjectName);

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        List errors = sportObjectPositionDtoResponseEntity.getBody();
        assertField("name", ErrorCode.EMPTY.getCode(), errors);
        assertField("positionsCount", ErrorCode.EMPTY.getCode(), errors);
    }
}
