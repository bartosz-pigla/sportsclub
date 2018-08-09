package integrationTest.adminApi.sportObject.openingTime;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.ADMIN_CONSOLE_OPENING_TIME_BY_ID;

import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.openingTime.command.DeleteOpeningTimeCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import integrationTest.adminApi.sportObject.AbstractSportObjectItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import web.adminApi.sportObject.dto.OpeningTimeRangeDto;

public final class DeleteOpeningTimeItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldDeleteWhenExistsAndNotDeleted() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        createOpeningTime(sportObjectRepository.findAll().get(0).getId());
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        UUID openingTimeId = openingTimeRepository.findAll().get(0).getId();

        ResponseEntity<OpeningTimeRangeDto> deleteOpeningTimeRangeDtoResponseEntity = delete(
                ADMIN_CONSOLE_OPENING_TIME_BY_ID,
                OpeningTimeRangeDto.class,
                sportsclubName, sportObjectName, openingTimeId.toString());

        assertEquals(deleteOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.OK);
        OpeningTimeRangeDto deletedOpeningTime = deleteOpeningTimeRangeDtoResponseEntity.getBody();
        assertTrue(openingTimeRepository.findById(UUID.fromString(deletedOpeningTime.getId())).get().isDeleted());
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenNotExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        createOpeningTime(sportObjectRepository.findAll().get(0).getId());
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        UUID openingTimeId = openingTimeRepository.findAll().get(0).getId();

        ResponseEntity<Object> deleteOpeningTimeRangeDtoResponseEntity = delete(
                ADMIN_CONSOLE_OPENING_TIME_BY_ID,
                Object.class,
                sportsclubName, sportObjectName, "notExistingOpeningTimeId");

        assertEquals(deleteOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(openingTimeRepository.findByIdAndDeletedFalse(openingTimeId).isPresent());
    }

    @Test
    @DirtiesContext
    public void shouldNotDeleteWhenIsAlreadyDeleted() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        createOpeningTime(sportObjectRepository.findAll().get(0).getId());
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        UUID sportObjectId = sportObjectRepository.findByNameAndDeletedFalse(createSportObjectCommand.getName()).get().getId();
        UUID openingTimeId = openingTimeRepository.findAll().get(0).getId();

        commandGateway.sendAndWait(DeleteOpeningTimeCommand.builder()
                .sportObjectId(sportObjectId)
                .openingTimeId(openingTimeId).build());

        ResponseEntity<List> deleteOpeningTimeRangeDtoResponseEntity = delete(
                ADMIN_CONSOLE_OPENING_TIME_BY_ID,
                List.class,
                sportsclubName, sportObjectName, openingTimeId.toString());

        assertEquals(deleteOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(openingTimeRepository.findByIdAndDeletedFalse(openingTimeId).isPresent());
    }
}
