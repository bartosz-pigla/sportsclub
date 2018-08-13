package integrationTest.adminApi.sportObject;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static query.model.sportobject.repository.SportObjectQueryExpressions.nameMatches;
import static web.common.RequestMappings.ADMIN_CONSOLE_SPORT_OBJECT;

import java.net.MalformedURLException;
import java.util.List;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import commons.ErrorCode;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import web.adminApi.sportObject.dto.AddressDto;
import web.adminApi.sportObject.dto.SportObjectDto;

public final class CreateSportObjectItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldCreateWhenSportsclubIdExists() {
        String sportsclubName = createSportsclub().getName();
        String sportObjectName = "name1";
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name(sportObjectName)
                .sportsclubName(sportsclubName)
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<SportObjectDto> createSportObjectResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_SPORT_OBJECT,
                sportObject,
                SportObjectDto.class,
                sportsclubName);

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.OK);

        SportObjectDto response = createSportObjectResponse.getBody();
        assertEquals(response.getName(), sportObject.getName());
        assertEquals(response.getSportsclubName(), sportObject.getSportsclubName());
        assertTrue(sportObjectRepository.exists(nameMatches(sportObjectName)));
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenSportsclubNameNotExists() {
        String sportsclubName = "notExistingSportsclub";
        String sportObjectName = "name1";
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name(sportObjectName)
                .sportsclubName(sportsclubName)
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<List> createSportObjectResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_SPORT_OBJECT,
                sportObject,
                List.class,
                sportsclubName);

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertFalse(sportObjectRepository.exists(nameMatches(sportObjectName)));
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenSportObjectWithGivenNameAlreadyExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name(sportObjectName)
                .sportsclubName(sportsclubName)
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<List> createSportObjectResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_SPORT_OBJECT,
                sportObject,
                List.class,
                sportsclubName);

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.CONFLICT);
        assertField("name", ErrorCode.ALREADY_EXISTS.getCode(), createSportObjectResponse.getBody());
        assertTrue(sportObjectRepository.exists(nameMatches(sportObjectName)));
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateWhenSportObjectWithGivenNameIsEmpty() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .sportsclubName(sportsclubName)
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<List> createSportObjectResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_SPORT_OBJECT,
                sportObject,
                List.class,
                sportsclubName);

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertField("name", ErrorCode.EMPTY.getCode(), createSportObjectResponse.getBody());
        assertTrue(sportObjectRepository.exists(nameMatches(sportObjectName)));
    }
}
