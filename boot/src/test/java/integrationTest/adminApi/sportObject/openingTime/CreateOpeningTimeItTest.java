package integrationTest.adminApi.sportObject.openingTime;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static web.common.RequestMappings.DIRECTOR_API_OPENING_TIME;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

import commons.ErrorCode;
import integrationTest.adminApi.sportObject.AbstractSportObjectItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import web.sportObject.dto.OpeningTimeDto;
import web.sportObject.dto.TimeDto;

public final class CreateOpeningTimeItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldCreateWhenSportObjectExistsAndOpeningTimeIsValid() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        signIn("superuser", "password");

        OpeningTimeDto openingTimeDto = OpeningTimeDto.builder()
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(TimeDto.builder()
                        .hour(10)
                        .minute(0).build())
                .finishTime(TimeDto.builder()
                        .hour(12)
                        .minute(0).build()).build();

        ResponseEntity<OpeningTimeDto> createOpeningTimeRangeDtoResponseEntity = restTemplate.postForEntity(
                DIRECTOR_API_OPENING_TIME,
                openingTimeDto,
                OpeningTimeDto.class,
                sportsclubId.toString(),
                sportObjectId.toString());

        assertEquals(createOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.OK);

        OpeningTimeDto response = createOpeningTimeRangeDtoResponseEntity.getBody();
        TimeDto startTime = response.getStartTime();
        TimeDto finishTime = response.getFinishTime();
        assertEquals(response.getDayOfWeek(), openingTimeDto.getDayOfWeek());
        assertEquals(startTime.getHour(), openingTimeDto.getStartTime().getHour());
        assertEquals(startTime.getMinute(), openingTimeDto.getStartTime().getMinute());
        assertEquals(finishTime.getHour(), openingTimeDto.getFinishTime().getHour());
        assertEquals(finishTime.getMinute(), openingTimeDto.getFinishTime().getMinute());
        assertTrue(exists(openingTimeDto));
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenSportObjectNotExists() {
        String sportsclubId = createSportsclub().toString();
        String sportObjectId = "notExistingSportObject";

        signIn("superuser", "password");

        OpeningTimeDto openingTimeDto = OpeningTimeDto.builder()
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(TimeDto.builder()
                        .hour(10)
                        .minute(0).build())
                .finishTime(TimeDto.builder()
                        .hour(12)
                        .minute(0).build()).build();

        ResponseEntity<String> createOpeningTimeRangeDtoResponseEntity = restTemplate.postForEntity(
                DIRECTOR_API_OPENING_TIME,
                openingTimeDto,
                String.class,
                sportsclubId,
                sportObjectId);

        assertEquals(createOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(exists(openingTimeDto));
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenOpeningTimeIsInvalid() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        createOpeningTime(sportObjectId);
        signIn("superuser", "password");

        OpeningTimeDto openingTimeDto = OpeningTimeDto.builder()
                .price(10.33d)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(TimeDto.builder()
                        .hour(10)
                        .minute(0).build())
                .finishTime(TimeDto.builder()
                        .hour(12)
                        .minute(0).build()).build();

        ResponseEntity<List> createOpeningTimeRangeDtoResponseEntity = restTemplate.postForEntity(
                DIRECTOR_API_OPENING_TIME,
                openingTimeDto,
                List.class,
                sportsclubId.toString(),
                sportObjectId.toString());

        assertEquals(createOpeningTimeRangeDtoResponseEntity.getStatusCode(), HttpStatus.CONFLICT);
        assertField("openingTimeRange", ErrorCode.ALREADY_EXISTS.getCode(), createOpeningTimeRangeDtoResponseEntity.getBody());
        assertFalse(exists(openingTimeDto));
    }
}
