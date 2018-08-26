package integrationTest.adminApi.sportObject;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static web.common.RequestMappings.DIRECTOR_API_SPORT_OBJECT_BY_ID;

import java.util.List;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.command.DeleteSportObjectCommand;
import commons.ErrorCode;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.model.embeddable.ImageUrl;
import query.model.sportobject.repository.SportObjectQueryExpressions;
import web.adminApi.sportObject.dto.AddressDto;
import web.adminApi.sportObject.dto.SportObjectDto;

public final class UpdateSportObjectItTest extends AbstractSportObjectItTest {

    @Test
    @DirtiesContext
    public void shouldUpdateWhenSportsclubAndSportObjectExists() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name("name1")
                .sportsclubId(sportsclubId.toString())
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<SportObjectDto> createSportObjectResponse = put(
                DIRECTOR_API_SPORT_OBJECT_BY_ID,
                sportObject,
                SportObjectDto.class,
                sportsclubId.toString(),
                sportObjectId.toString());

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.NO_CONTENT);

        assertTrue(sportObjectRepository.exists(
                SportObjectQueryExpressions.idMatches(sportObjectId)));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportsclubNameNotExists() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name("name1")
                .sportsclubId(sportsclubId.toString())
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<SportObjectDto> createSportObjectResponse = put(
                DIRECTOR_API_SPORT_OBJECT_BY_ID,
                sportObject,
                SportObjectDto.class,
                "notExistingSportsclubId",
                sportObjectId.toString());

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportObjectNameNotExists() {
        UUID sportsclubId = createSportsclub();
        createSportObject(sportsclubId);
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name("name1")
                .sportsclubId(sportsclubId.toString())
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<SportObjectDto> createSportObjectResponse = put(
                DIRECTOR_API_SPORT_OBJECT_BY_ID,
                sportObject,
                SportObjectDto.class,
                sportsclubId.toString(),
                "notExistingSportObjectId");

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportObjectWithGivenNameAlreadyExists() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        createAnotherSportObject(sportsclubId);
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name("name2")
                .sportsclubId(sportsclubId.toString())
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg")
                .build();

        ResponseEntity<List> createSportObjectResponse = put(
                DIRECTOR_API_SPORT_OBJECT_BY_ID,
                sportObject,
                List.class,
                sportsclubId.toString(),
                sportObjectId.toString());

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.CONFLICT);
        assertField("name", ErrorCode.ALREADY_EXISTS.getCode(), createSportObjectResponse.getBody());

        assertTrue(sportObjectRepository.exists(
                SportObjectQueryExpressions.idMatches(sportObjectId)));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateWhenSportObjectWithGivenNameIsEmpty() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .sportsclubId(sportsclubId.toString())
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg").build();

        ResponseEntity<List> createSportObjectResponse = put(
                DIRECTOR_API_SPORT_OBJECT_BY_ID,
                sportObject,
                List.class,
                sportsclubId.toString(),
                sportObjectId.toString());

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertField("name", ErrorCode.EMPTY.getCode(), createSportObjectResponse.getBody());

        assertTrue(sportObjectRepository.exists(
                SportObjectQueryExpressions.idMatches(sportObjectId)));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateAlreadyDeleted() {
        UUID sportsclubId = createSportsclub();
        UUID sportObjectId = createSportObject(sportsclubId);
        commandGateway.sendAndWait(new DeleteSportObjectCommand(sportObjectId));
        signIn("superuser", "password");

        SportObjectDto sportObject = SportObjectDto.builder()
                .name("name1")
                .sportsclubId(sportsclubId.toString())
                .address(new AddressDto("street", "city", 1.1d, 1.1d))
                .description("description1")
                .imageUrl("https://www.w3schools.com/w3css/img_lights.jpg")
                .build();

        ResponseEntity<List> createSportObjectResponse = put(
                DIRECTOR_API_SPORT_OBJECT_BY_ID,
                sportObject,
                List.class,
                sportsclubId.toString(),
                sportObjectId.toString());

        assertEquals(createSportObjectResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    private void createAnotherSportObject(UUID sportsclubId) {
        String name = "name2";

        CreateSportObjectCommand createSportObjectCommand = CreateSportObjectCommand.builder()
                .sportsclubId(sportsclubId)
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .description("description1")
                .imageUrl(new ImageUrl("https://www.w3schools.com/w3css/img_lights.jpg"))
                .name(name)
                .build();

        commandGateway.sendAndWait(createSportObjectCommand);
    }
}
