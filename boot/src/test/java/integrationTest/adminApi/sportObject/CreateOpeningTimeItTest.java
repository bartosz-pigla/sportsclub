package integrationTest.adminApi.sportObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.ADMIN_CONSOLE_OPENING_TIME;

import java.net.MalformedURLException;
import java.time.DayOfWeek;
import java.util.List;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import commons.ErrorCode;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import web.adminApi.sportObject.dto.OpeningTimeDto;
import web.adminApi.sportObject.dto.OpeningTimeRangeDto;

public final class CreateOpeningTimeItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldCreateWhenSportObjectExistsAndOpeningTimeIsValid() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();

        OpeningTimeRangeDto openingTimeRangeDto = OpeningTimeRangeDto.builder()
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(OpeningTimeDto.builder()
                        .hour(10)
                        .minute(0).build())
                .finishTime(OpeningTimeDto.builder()
                        .hour(12)
                        .minute(0).build()).build();

        ResponseEntity<OpeningTimeRangeDto> createOpeningTimeRangeDtoResponseEntity = restTemplate.postForEntity(
                ADMIN_CONSOLE_OPENING_TIME,
                openingTimeRangeDto,
                OpeningTimeRangeDto.class,
                sportsclubName, sportObjectName);

        assertEquals(createOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.OK);

        OpeningTimeRangeDto response = createOpeningTimeRangeDtoResponseEntity.getBody();
        OpeningTimeDto startTime = response.getStartTime();
        OpeningTimeDto finishTime = response.getFinishTime();
        assertEquals(response.getDayOfWeek(), openingTimeRangeDto.getDayOfWeek());
        assertEquals(startTime.getHour(), openingTimeRangeDto.getStartTime().getHour());
        assertEquals(startTime.getMinute(), openingTimeRangeDto.getStartTime().getMinute());
        assertEquals(finishTime.getHour(), openingTimeRangeDto.getFinishTime().getHour());
        assertEquals(finishTime.getMinute(), openingTimeRangeDto.getFinishTime().getMinute());
        Object obj = openingTimeRepository.findAll();
        assertTrue(exists(openingTimeRangeDto));
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenSportObjectNotExists() {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = "notExistingSportObject";

        OpeningTimeRangeDto openingTimeRangeDto = OpeningTimeRangeDto.builder()
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(OpeningTimeDto.builder()
                        .hour(10)
                        .minute(0).build())
                .finishTime(OpeningTimeDto.builder()
                        .hour(12)
                        .minute(0).build()).build();

        ResponseEntity<Object> createOpeningTimeRangeDtoResponseEntity = restTemplate.postForEntity(
                ADMIN_CONSOLE_OPENING_TIME,
                openingTimeRangeDto,
                Object.class,
                sportsclubName, sportObjectName);

        assertEquals(createOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(exists(openingTimeRangeDto));
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenOpeningTimeIsInvalid() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        createOpeningTime(sportObjectRepository.findAll().get(0).getId());
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();

        OpeningTimeRangeDto openingTimeRangeDto = OpeningTimeRangeDto.builder()
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(OpeningTimeDto.builder()
                        .hour(10)
                        .minute(0).build())
                .finishTime(OpeningTimeDto.builder()
                        .hour(12)
                        .minute(0).build()).build();

        ResponseEntity<List> createOpeningTimeRangeDtoResponseEntity = restTemplate.postForEntity(
                ADMIN_CONSOLE_OPENING_TIME,
                openingTimeRangeDto,
                List.class,
                sportsclubName, sportObjectName);

        assertEquals(createOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.CONFLICT);
        assertField("openingTimeRange", ErrorCode.ALREADY_EXISTS.getCode(), createOpeningTimeRangeDtoResponseEntity.getBody());
        assertFalse(exists(openingTimeRangeDto));
    }
}
