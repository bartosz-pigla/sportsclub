package integrationTest.adminApi.sportObject.sportObjectPosition;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME;

import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.sportObjectPosition.command.CreateSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.command.DeleteSportObjectPositionCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import commons.ErrorCode;
import integrationTest.adminApi.sportObject.AbstractSportObjectItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.embeddable.PositionsCount;
import web.adminApi.sportObject.dto.SportObjectPositionDto;

public final class UpdateSportObjectPostionItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldUpdateWhenExistsAndIsNotDeletedAndNameIsUnique() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        UUID sportObjectId = sportObjectRepository.findByNameAndDeletedFalse(createSportObjectCommand.getName()).get().getId();
        CreateSportObjectPositionCommand createSportObjectPositionCommand = createSportObjectPosition(sportObjectId);
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        String sportObjectPositionName = createSportObjectPositionCommand.getName();
        SportObjectPositionDto position = SportObjectPositionDto.builder()
                .name("name2")
                .description("description2")
                .positionsCount(2)
                .build();

        ResponseEntity<SportObjectPositionDto> sportObjectPositionDtoResponseEntity = put(
                ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME,
                position,
                SportObjectPositionDto.class,
                sportsclubName, sportObjectName, sportObjectPositionName);

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.OK);
        SportObjectPositionDto responseBody = sportObjectPositionDtoResponseEntity.getBody();

        assertEquals(position.getName(), responseBody.getName());
        assertEquals(position.getDescription(), responseBody.getDescription());
        assertEquals(position.getPositionsCount(), responseBody.getPositionsCount());
        assertTrue(sportObjectPositionRepository.findByNameAndDeletedFalse(position.getName()).isPresent());
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenNameAlreadyExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        UUID sportObjectId = sportObjectRepository.findByNameAndDeletedFalse(createSportObjectCommand.getName()).get().getId();
        CreateSportObjectPositionCommand createSportObjectPositionCommand = createSportObjectPosition(sportObjectId);
        signIn("superuser", "password");

        commandGateway.sendAndWait(CreateSportObjectPositionCommand.builder()
                .sportObjectId(sportObjectId)
                .name("name2")
                .description("description2")
                .positionsCount(new PositionsCount(12))
                .build());

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        String sportObjectPositionName = createSportObjectPositionCommand.getName();
        SportObjectPositionDto position = SportObjectPositionDto.builder()
                .name("name2")
                .description("description2")
                .positionsCount(2)
                .build();

        ResponseEntity<List> sportObjectPositionDtoResponseEntity = put(
                ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME,
                position,
                List.class,
                sportsclubName, sportObjectName, sportObjectPositionName);

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.CONFLICT);

        List errors = sportObjectPositionDtoResponseEntity.getBody();
        assertField("name", ErrorCode.ALREADY_EXISTS.getCode(), errors);
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenIsAlreadyDeleted() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        UUID sportObjectId = sportObjectRepository.findByNameAndDeletedFalse(createSportObjectCommand.getName()).get().getId();
        CreateSportObjectPositionCommand createSportObjectPositionCommand = createSportObjectPosition(sportObjectId);
        UUID sportObjectPositionId = sportObjectPositionRepository.findByNameAndDeletedFalse(createSportObjectCommand.getName()).get().getId();
        commandGateway.sendAndWait(new DeleteSportObjectPositionCommand(sportObjectId, sportObjectPositionId));
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        String sportObjectPositionName = createSportObjectPositionCommand.getName();
        SportObjectPositionDto position = SportObjectPositionDto.builder()
                .name("name1")
                .description("description2")
                .positionsCount(2)
                .build();

        ResponseEntity<List> sportObjectPositionDtoResponseEntity = put(
                ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME,
                position,
                List.class,
                sportsclubName, sportObjectName, sportObjectPositionName);

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(sportObjectPositionRepository.findByNameAndDeletedFalse(position.getName()).isPresent());
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenNotExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        SportObjectPositionDto position = SportObjectPositionDto.builder()
                .name("name1")
                .description("description2")
                .positionsCount(2)
                .build();

        ResponseEntity<Object> sportObjectPositionDtoResponseEntity = put(
                ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME,
                position,
                Object.class,
                sportsclubName, sportObjectName, "notExistingSportObjectPositionName");

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(sportObjectPositionRepository.findByNameAndDeletedFalse(position.getName()).isPresent());
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenNameAndPositionsCountIsEmpty() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        UUID sportObjectId = sportObjectRepository.findByNameAndDeletedFalse(createSportObjectCommand.getName()).get().getId();
        CreateSportObjectPositionCommand createSportObjectPositionCommand = createSportObjectPosition(sportObjectId);
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        String sportObjectPositionName = createSportObjectPositionCommand.getName();
        SportObjectPositionDto position = new SportObjectPositionDto();

        ResponseEntity<List> sportObjectPositionDtoResponseEntity = put(
                ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME,
                position,
                List.class,
                sportsclubName, sportObjectName, sportObjectPositionName);

        assertEquals(sportObjectPositionDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        List errors = sportObjectPositionDtoResponseEntity.getBody();
        assertField("name", ErrorCode.EMPTY.getCode(), errors);
        assertField("positionsCount", ErrorCode.EMPTY.getCode(), errors);
        assertFalse(sportObjectPositionRepository.findByNameAndDeletedFalse(position.getName()).isPresent());
    }
}
