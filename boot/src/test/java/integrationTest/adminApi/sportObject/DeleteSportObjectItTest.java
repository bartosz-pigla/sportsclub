package integrationTest.adminApi.sportObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static query.model.sportobject.repository.SportObjectQueryExpressions.nameMatches;
import static web.common.RequestMappings.ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME;

import java.net.MalformedURLException;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import web.adminApi.sportObject.dto.SportObjectDto;

public final class DeleteSportObjectItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldDeleteWhenSportsclubAndSportObjectExistsAndIsNotDeleted() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        signIn("superuser", "password");

        ResponseEntity<SportObjectDto> deleteSportObjectResponse = delete(
                ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME,
                SportObjectDto.class,
                sportsclubName, sportObjectName);

        assertEquals(deleteSportObjectResponse.getStatusCode(), HttpStatus.OK);

        SportObjectDto sportObjectDto = deleteSportObjectResponse.getBody();
        assertEquals(sportObjectDto.getSportsclubName(), sportsclubName);
        assertEquals(sportObjectDto.getName(), sportObjectName);
        assertFalse(sportObjectRepository.exists(nameMatches(sportObjectName)));
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenSportsclubNotExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);

        String sportsclubName = "notExistingSportsclubName";
        String sportObjectName = createSportObjectCommand.getName();
        signIn("superuser", "password");

        ResponseEntity<Object> deleteSportObjectResponse = delete(
                ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME,
                Object.class,
                sportsclubName, sportObjectName);

        assertEquals(deleteSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenSportObjectNotExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = "notExistingSportObjectName";
        signIn("superuser", "password");

        ResponseEntity<Object> deleteSportObjectResponse = delete(
                ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME,
                Object.class,
                sportsclubName, sportObjectName);

        assertEquals(deleteSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);

    }
}
