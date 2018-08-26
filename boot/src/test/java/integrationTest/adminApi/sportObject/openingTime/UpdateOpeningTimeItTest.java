package integrationTest.adminApi.sportObject.openingTime;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.DIRECTOR_API_OPENING_TIME_BY_ID;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import api.sportObject.openingTime.command.CreateOpeningTimeCommand;
import commons.ErrorCode;
import integrationTest.adminApi.sportObject.AbstractSportObjectItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.Price;
import web.adminApi.sportObject.dto.OpeningTimeDto;
import web.adminApi.sportObject.dto.TimeDto;

public final class UpdateOpeningTimeItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldUpdateWhenSportObjectExistsAndOpeningTimeIsValid() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);

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
        UUID openingTimeId = openingTimeRepository.findAll().get(0).getId();

        OpeningTimeDto openingTimeDto = OpeningTimeDto.builder()
                .id(openingTimeId.toString())
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(TimeDto.builder()
                        .hour(10)
                        .minute(0)
                        .build())
                .finishTime(TimeDto.builder()
                        .hour(12)
                        .minute(0)
                        .build())
                .build();

        ResponseEntity<OpeningTimeDto> updateOpeningTimeRangeDtoResponseEntity = put(
                DIRECTOR_API_OPENING_TIME_BY_ID,
                openingTimeDto,
                OpeningTimeDto.class,
                sportsclubId.toString(),
                sportObjectId.toString(),
                openingTimeId.toString());

        assertEquals(updateOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
        assertTrue(exists(openingTimeDto));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportsclubNotExists() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        UUID openingTimeId = createOpeningTime(sportObjectId);
        signIn("superuser", "password");

        OpeningTimeDto openingTimeDto = OpeningTimeDto.builder()
                .id(openingTimeId.toString())
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(TimeDto.builder()
                        .hour(10)
                        .minute(0)
                        .build())
                .finishTime(TimeDto.builder()
                        .hour(12)
                        .minute(0)
                        .build())
                .build();

        ResponseEntity<String> updateOpeningTimeRangeDtoResponseEntity = put(
                DIRECTOR_API_OPENING_TIME_BY_ID,
                openingTimeDto,
                String.class,
                "notExistingSportsclub",
                sportObjectId.toString(),
                openingTimeId.toString());

        assertEquals(updateOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(exists(openingTimeDto));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportObjectNotExists() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        UUID openingTimeId = createOpeningTime(sportObjectId);
        signIn("superuser", "password");

        OpeningTimeDto openingTimeDto = OpeningTimeDto.builder()
                .id(openingTimeId.toString())
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(TimeDto.builder()
                        .hour(10)
                        .minute(0)
                        .build())
                .finishTime(TimeDto.builder()
                        .hour(12)
                        .minute(0)
                        .build())
                .build();

        ResponseEntity<String> updateOpeningTimeRangeDtoResponseEntity = put(
                DIRECTOR_API_OPENING_TIME_BY_ID,
                openingTimeDto,
                String.class,
                sportsclubId.toString(),
                "notExistingSportObjectId",
                openingTimeId.toString());

        assertEquals(updateOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(exists(openingTimeDto));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenOpeningTimeIsInvalid() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);

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

        UUID openingTimeId = openingTimeRepository.findAll().get(0).getId();

        OpeningTimeDto openingTimeDto = OpeningTimeDto.builder()
                .id(openingTimeId.toString())
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(TimeDto.builder()
                        .hour(9)
                        .minute(0).build())
                .finishTime(TimeDto.builder()
                        .hour(13)
                        .minute(30).build()).build();

        ResponseEntity<List> updateOpeningTimeRangeDtoResponseEntity = put(
                DIRECTOR_API_OPENING_TIME_BY_ID,
                openingTimeDto,
                List.class,
                sportsclubId,
                sportObjectId,
                openingTimeId.toString());

        assertEquals(updateOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.CONFLICT);
        assertField("openingTimeRange", ErrorCode.ALREADY_EXISTS.getCode(), updateOpeningTimeRangeDtoResponseEntity.getBody());
        assertFalse(exists(openingTimeDto));
    }
}
