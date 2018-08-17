package integrationTest.adminApi.sportObject.openingTime;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.ADMIN_API_OPENING_TIME_BY_ID;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.openingTime.command.CreateOpeningTimeCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import commons.ErrorCode;
import integrationTest.adminApi.sportObject.AbstractSportObjectItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.Price;
import web.adminApi.sportObject.dto.OpeningTimeDto;
import web.adminApi.sportObject.dto.OpeningTimeRangeDto;

public final class UpdateOpeningTimeItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldUpdateWhenSportObjectExistsAndOpeningTimeIsValid() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        UUID sportObjectId = sportObjectRepository.findAll().get(0).getId();

        CreateOpeningTimeCommand createFirstOpeningTimeCommand = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectId)
                .price(new Price(new BigDecimal(12.23d)))
                .timeRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0)))
                .build();

        CreateOpeningTimeCommand createSecondOpeningTimeCommand = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectId)
                .price(new Price(new BigDecimal(12.23d)))
                .timeRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(13, 0), LocalTime.of(14, 0)))
                .build();

        commandGateway.sendAndWait(createFirstOpeningTimeCommand);
        commandGateway.sendAndWait(createSecondOpeningTimeCommand);

        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        UUID openingTimeId = openingTimeRepository.findAll().get(0).getId();

        OpeningTimeRangeDto openingTimeRangeDto = OpeningTimeRangeDto.builder()
                .id(openingTimeId.toString())
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(OpeningTimeDto.builder()
                        .hour(10)
                        .minute(0).build())
                .finishTime(OpeningTimeDto.builder()
                        .hour(12)
                        .minute(0).build()).build();

        ResponseEntity<OpeningTimeRangeDto> updateOpeningTimeRangeDtoResponseEntity = put(
                ADMIN_API_OPENING_TIME_BY_ID,
                openingTimeRangeDto,
                OpeningTimeRangeDto.class,
                sportsclubName, sportObjectName, openingTimeId.toString());

        assertEquals(updateOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(exists(openingTimeRangeDto));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportsclubNotExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        createOpeningTime(sportObjectRepository.findAll().get(0).getId());
        signIn("superuser", "password");

        String sportsclubName = "notExistingSportsclub";
        String sportObjectName = createSportObjectCommand.getName();
        UUID openingTimeId = openingTimeRepository.findAll().get(0).getId();

        OpeningTimeRangeDto openingTimeRangeDto = OpeningTimeRangeDto.builder()
                .id(openingTimeId.toString())
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(OpeningTimeDto.builder()
                        .hour(10)
                        .minute(0).build())
                .finishTime(OpeningTimeDto.builder()
                        .hour(12)
                        .minute(0).build()).build();

        ResponseEntity<List> updateOpeningTimeRangeDtoResponseEntity = put(
                ADMIN_API_OPENING_TIME_BY_ID,
                openingTimeRangeDto,
                List.class,
                sportsclubName, sportObjectName, openingTimeId.toString());

        assertEquals(updateOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(exists(openingTimeRangeDto));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportObjectNotExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        createSportObject(createSportsclubCommand);
        createOpeningTime(sportObjectRepository.findAll().get(0).getId());
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = "notExistingSportObject";
        UUID openingTimeId = openingTimeRepository.findAll().get(0).getId();

        OpeningTimeRangeDto openingTimeRangeDto = OpeningTimeRangeDto.builder()
                .id(openingTimeId.toString())
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(OpeningTimeDto.builder()
                        .hour(10)
                        .minute(0).build())
                .finishTime(OpeningTimeDto.builder()
                        .hour(12)
                        .minute(0).build()).build();

        ResponseEntity<List> updateOpeningTimeRangeDtoResponseEntity = put(
                ADMIN_API_OPENING_TIME_BY_ID,
                openingTimeRangeDto,
                List.class,
                sportsclubName, sportObjectName, openingTimeId.toString());

        assertEquals(updateOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(exists(openingTimeRangeDto));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenOpeningTimeIsInvalid() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        UUID sportObjectId = sportObjectRepository.findAll().get(0).getId();

        CreateOpeningTimeCommand createFirstOpeningTimeCommand = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectId)
                .price(new Price(new BigDecimal(12.23d)))
                .timeRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0)))
                .build();

        CreateOpeningTimeCommand createSecondOpeningTimeCommand = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectId)
                .price(new Price(new BigDecimal(12.23d)))
                .timeRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(13, 0), LocalTime.of(14, 0)))
                .build();

        commandGateway.sendAndWait(createFirstOpeningTimeCommand);
        commandGateway.sendAndWait(createSecondOpeningTimeCommand);
        signIn("superuser", "password");

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        UUID openingTimeId = openingTimeRepository.findAll().get(0).getId();

        OpeningTimeRangeDto openingTimeRangeDto = OpeningTimeRangeDto.builder()
                .id(openingTimeId.toString())
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(OpeningTimeDto.builder()
                        .hour(9)
                        .minute(0).build())
                .finishTime(OpeningTimeDto.builder()
                        .hour(13)
                        .minute(30).build()).build();

        ResponseEntity<List> updateOpeningTimeRangeDtoResponseEntity = put(
                ADMIN_API_OPENING_TIME_BY_ID,
                openingTimeRangeDto,
                List.class,
                sportsclubName, sportObjectName, openingTimeId.toString());

        assertEquals(updateOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.CONFLICT);
        assertField("openingTimeRange", ErrorCode.ALREADY_EXISTS.getCode(), updateOpeningTimeRangeDtoResponseEntity.getBody());
        assertFalse(exists(openingTimeRangeDto));
    }
}
