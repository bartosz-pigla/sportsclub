package integrationTest.adminApi.sportObject;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static web.common.RequestMappings.ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.command.DeleteSportObjectCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import commons.ErrorCode;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.model.sportobject.repository.SportObjectQueryExpressions;
import query.model.sportsclub.repository.SportsclubQueryExpressions;
import web.adminApi.sportObject.dto.AddressDto;
import web.adminApi.sportObject.dto.SportObjectDto;

public final class UpdateSportObjectItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldUpdateWhenSportsclubAndSportObjectExists() throws MalformedURLException {
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

        ResponseEntity<SportObjectDto> createSportObjectResponse = put(
                ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME,
                sportObject,
                SportObjectDto.class,
                sportsclubName, sportObjectName);

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.OK);

        SportObjectDto response = createSportObjectResponse.getBody();
        assertEquals(response.getName(), sportObject.getName());
        assertEquals(response.getSportsclubName(), sportObject.getSportsclubName());

        assertTrue(sportObjectRepository.exists(
                SportObjectQueryExpressions.nameMatches(sportObjectName)));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportsclubNameNotExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);

        String sportsclubName = "notExistingSportsclub";
        String sportObjectName = createSportObjectCommand.getName();
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name(sportObjectName)
                .sportsclubName(sportsclubName)
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<SportObjectDto> createSportObjectResponse = put(
                ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME,
                sportObject,
                SportObjectDto.class,
                sportsclubName, sportObjectName);

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportObjectNameNotExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);

        String sportsclubName = "notExistingSportObject";
        String sportObjectName = createSportObjectCommand.getName();
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name(sportObjectName)
                .sportsclubName(sportsclubName)
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<SportObjectDto> createSportObjectResponse = put(
                ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME,
                sportObject,
                SportObjectDto.class,
                sportsclubName, sportObjectName);

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportObjectWithGivenNameAlreadyExists() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);
        CreateSportObjectCommand createAnotherSportObjectCommand = createAnotherSportObject(createSportsclubCommand);

        Object obj = sportObjectRepository.findAll();

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name(createAnotherSportObjectCommand.getName())
                .sportsclubName(sportsclubName)
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<List> createSportObjectResponse = put(
                ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME,
                sportObject,
                List.class,
                sportsclubName, sportObjectName);

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.CONFLICT);
        assertField("name", ErrorCode.ALREADY_EXISTS.getCode(), createSportObjectResponse.getBody());

        assertTrue(sportObjectRepository.exists(
                SportObjectQueryExpressions.nameMatches(sportObjectName)));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportObjectWithGivenNameIsEmpty() throws MalformedURLException {
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

        ResponseEntity<List> createSportObjectResponse = put(
                ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME,
                sportObject,
                List.class,
                sportsclubName, sportObjectName);

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertField("name", ErrorCode.EMPTY.getCode(), createSportObjectResponse.getBody());

        assertTrue(sportObjectRepository.exists(
                SportObjectQueryExpressions.nameMatches(sportObjectName)));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateAlreadyDeleted() throws MalformedURLException {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        CreateSportObjectCommand createSportObjectCommand = createSportObject(createSportsclubCommand);

        UUID sportObjectId = sportObjectRepository.findOne(
                SportObjectQueryExpressions.nameMatches(createSportObjectCommand.getName())).get().getId();

        DeleteSportObjectCommand deleteSportObjectCommand = DeleteSportObjectCommand.builder()
                .sportObjectId(sportObjectId)
                .build();
        commandGateway.sendAndWait(deleteSportObjectCommand);

        String sportsclubName = createSportsclubCommand.getName();
        String sportObjectName = createSportObjectCommand.getName();
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name(sportObjectName)
                .sportsclubName(sportsclubName)
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<List> createSportObjectResponse = put(
                ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME,
                sportObject,
                List.class,
                sportsclubName, sportObjectName);

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    private CreateSportObjectCommand createAnotherSportObject(CreateSportsclubCommand createSportsclubCommand) throws MalformedURLException {
        UUID sportsclubId = sportsclubRepository.findOne(
                SportsclubQueryExpressions.nameMatches(createSportsclubCommand.getName())).get().getId();

        CreateSportObjectCommand createSportObjectCommand = CreateSportObjectCommand.builder()
                .sportsclubId(sportsclubId)
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .description("description1")
                .image(new URL("https://www.w3schools.com/w3css/img_lights.jpg"))
                .name("name2").build();
        commandGateway.sendAndWait(createSportObjectCommand);
        return createSportObjectCommand;
    }
}
