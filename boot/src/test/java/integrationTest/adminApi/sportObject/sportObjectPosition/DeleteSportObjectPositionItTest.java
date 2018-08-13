package integrationTest.adminApi.sportObject.sportObjectPosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME;

import java.net.MalformedURLException;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.sportObjectPosition.command.CreateSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.command.DeleteSportObjectPositionCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import integrationTest.adminApi.sportObject.AbstractSportObjectItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.sportobject.repository.SportObjectPositionQueryExpressions;
import query.model.sportobject.repository.SportObjectQueryExpressions;

public final class DeleteSportObjectPositionItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldDeleteWhenIsNotDeletedAndExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        UUID sportObjectId = sportObjectRepository.findOne(SportObjectQueryExpressions.nameMatches(createSportObjectCommand.getName())).get().getId();
        CreateSportObjectPositionCommand createSportObjectPositionCommand = createSportObjectPosition(sportObjectId);
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        String sportObjectPositionName = createSportObjectPositionCommand.getName();

        ResponseEntity<Object> deletePositionResponseEntity = delete(
                ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME,
                Object.class,
                sportsclubName, sportObjectName, sportObjectPositionName);

        assertEquals(deletePositionResponseEntity.getStatusCode(), HttpStatus.OK);
        assertFalse(sportObjectPositionRepository.findOne(
                SportObjectPositionQueryExpressions.nameMatches(sportObjectPositionName)).isPresent());
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenNotExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        String sportObjectPositionName = "notExistingPosition";

        ResponseEntity<Object> deletePositionResponseEntity = delete(
                ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME,
                Object.class,
                sportsclubName, sportObjectName, sportObjectPositionName);

        assertEquals(deletePositionResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(sportObjectPositionRepository.findOne(
                SportObjectPositionQueryExpressions.nameMatches(sportObjectPositionName)).isPresent());
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenIsAlreadyDeleted() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);

        UUID sportObjectId = sportObjectRepository.findOne(
                SportObjectQueryExpressions.nameMatches(createSportObjectCommand.getName())).get().getId();

        CreateSportObjectPositionCommand createSportObjectPositionCommand = createSportObjectPosition(sportObjectId);

        UUID sportObjectPositionId = sportObjectPositionRepository.findOne(
                SportObjectPositionQueryExpressions.nameMatches(createSportObjectPositionCommand.getName())).get().getId();

        commandGateway.sendAndWait(DeleteSportObjectPositionCommand.builder()
                .sportObjectId(sportObjectId)
                .sportObjectPositionId(sportObjectPositionId).build());
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        String sportObjectPositionName = createSportObjectPositionCommand.getName();

        ResponseEntity<Object> deletePositionResponseEntity = delete(
                ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME,
                Object.class,
                sportsclubName, sportObjectName, sportObjectPositionId);

        assertEquals(deletePositionResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(sportObjectPositionRepository.findOne(SportObjectPositionQueryExpressions.nameMatches(sportObjectPositionName)).isPresent());
    }
}
