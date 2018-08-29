package integrationTest.adminApi.sportObject;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static query.model.sportobject.repository.SportObjectQueryExpressions.nameMatches;
import static web.common.RequestMappings.DIRECTOR_API_SPORT_OBJECT;

import java.util.List;
import java.util.UUID;

import commons.ErrorCode;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import web.sportObject.dto.AddressDto;
import web.sportObject.dto.SportObjectDto;

public final class CreateSportObjectItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldCreateWhenSportsclubIdExists() {
        UUID sportsclubId = createSportsclub();
        String sportObjectName = "name1";
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name(sportObjectName)
                .sportsclubId(sportsclubId.toString())
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<SportObjectDto> createSportObjectResponse = restTemplate.postForEntity(
                DIRECTOR_API_SPORT_OBJECT,
                sportObject,
                SportObjectDto.class,
                sportsclubId.toString());

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.OK);

        SportObjectDto response = createSportObjectResponse.getBody();
        assertEquals(response.getName(), sportObject.getName());
        assertEquals(response.getSportsclubId(), sportObject.getSportsclubId());
        assertTrue(sportObjectRepository.exists(nameMatches(sportObjectName)));
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenSportsclubNameNotExists() {
        String sportsclubId = "notExistingSportsclubId";
        String sportObjectName = "name1";
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name(sportObjectName)
                .sportsclubId(sportsclubId)
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg")
                .build();

        ResponseEntity<String> createSportObjectResponse = restTemplate.postForEntity(
                DIRECTOR_API_SPORT_OBJECT,
                sportObject,
                String.class,
                sportsclubId);

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(sportObjectRepository.exists(nameMatches(sportObjectName)));
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenSportObjectWithGivenNameAlreadyExists() {
        UUID sportsclubId = createSportsclub();
        createSportObject(sportsclubId);
        signIn("superuser", "password");
        String sportObjectName = "name1";

        SportObjectDto sportObject = SportObjectDto.builder()
                .name(sportObjectName)
                .sportsclubId(sportsclubId.toString())
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg")
                .build();

        ResponseEntity<List> createSportObjectResponse = restTemplate.postForEntity(
                DIRECTOR_API_SPORT_OBJECT,
                sportObject,
                List.class,
                sportsclubId.toString());

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.CONFLICT);
        assertField("name", ErrorCode.ALREADY_EXISTS.getCode(), createSportObjectResponse.getBody());
        assertTrue(sportObjectRepository.exists(nameMatches(sportObjectName)));
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenSportObjectWithGivenNameIsEmpty() {
        UUID sportsclubId = createSportsclub();
        createSportObject(sportsclubId);
        String sportObjectName = "name1";
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .sportsclubId(sportsclubId.toString())
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg")
                .build();

        ResponseEntity<List> createSportObjectResponse = restTemplate.postForEntity(
                DIRECTOR_API_SPORT_OBJECT,
                sportObject,
                List.class,
                sportsclubId.toString());

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertField("name", ErrorCode.EMPTY.getCode(), createSportObjectResponse.getBody());
        assertTrue(sportObjectRepository.exists(nameMatches(sportObjectName)));
    }
}
