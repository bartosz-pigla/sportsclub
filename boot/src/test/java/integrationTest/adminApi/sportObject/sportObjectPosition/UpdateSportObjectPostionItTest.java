package integrationTest.adminApi.sportObject.sportObjectPosition;

import static org.junit.Assert.assertEquals;
import static web.common.RequestMappings.ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME;

import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.sportObjectPosition.command.CreateSportObjectPositionCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import commons.ErrorCode;
import integrationTest.adminApi.sportObject.AbstractSportObjectItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import web.adminApi.sportObject.dto.SportObjectPositionDto;

public final class UpdateSportObjectPostionItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldUpdateWhenExistsAndIsNotDeletedAndNameIsUnique() {

    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenNameAlreadyExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        UUID sportObjectId = sportObjectRepository.findByNameAndDeletedFalse(createSportObjectCommand.getName()).get().getId();
        CreateSportObjectPositionCommand createSportObjectPositionCommand = createSportObjectPosition(sportObjectId);
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        String sportObjectPositionName = createSportObjectPositionCommand.getName();
        SportObjectPositionDto position = new SportObjectPositionDto();

        ResponseEntity<List> sportObjectPositionDtoResponseEntity = restTemplate.postForEntity(
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
    public void shouldNotUpdateWhenIsAlreadyDeleted() {

    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenNotExists() {

    }
}
