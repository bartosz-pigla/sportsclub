package integrationTest.adminApi.sportsclub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static web.common.RequestMappings.ADMIN_CONSOLE_STATUTE;

import java.util.List;
import java.util.UUID;

import api.sportsclub.command.CreateSportsclubCommand;
import api.sportsclub.command.UpdateStatuteCommand;
import commons.ErrorCode;
import integrationTest.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.repository.SportsclubEntityRepository;
import web.adminApi.sportsclub.dto.StatuteDto;

public final class UpdateStatuteItTest extends IntegrationTest {

    @Autowired
    private SportsclubEntityRepository sportsclubRepository;

    @Test
    @DirtiesContext
    public void shouldAddStatuteWhenNotExistsAndSportsclubExists() {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        signIn("superuser", "password");
        StatuteDto statute = StatuteDto.builder()
                .sportsclubName(createSportsclubCommand.getName())
                .title("title1")
                .description("description1")
                .build();

        ResponseEntity<StatuteDto> addStatuteResponse = restTemplate.postForEntity(ADMIN_CONSOLE_STATUTE, statute, StatuteDto.class);
        assertEquals(addStatuteResponse.getStatusCode(), HttpStatus.OK);

        StatuteDto response = addStatuteResponse.getBody();
        assertEquals(statute.getTitle(), response.getTitle());
        assertEquals(statute.getDescription(), response.getDescription());
        assertEquals(statute.getSportsclubName(), response.getSportsclubName());
        assertNotNull(sportsclubRepository.findByName(createSportsclubCommand.getName()).get().getStatute());
    }

    @Test
    @DirtiesContext
    public void shouldUpdateStatuteWhenExistsAndSportsclubExists() {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        UpdateStatuteCommand updateStatuteCommand = createStatute(createSportsclubCommand);
        signIn("superuser", "password");
        StatuteDto statute = StatuteDto.builder()
                .sportsclubName(createSportsclubCommand.getName())
                .title("title2")
                .description("description2")
                .build();

        ResponseEntity<StatuteDto> addStatuteResponse = restTemplate.postForEntity(ADMIN_CONSOLE_STATUTE, statute, StatuteDto.class);

        assertEquals(addStatuteResponse.getStatusCode(), HttpStatus.OK);

        StatuteDto response = addStatuteResponse.getBody();
        assertEquals(statute.getTitle(), response.getTitle());
        assertEquals(statute.getDescription(), response.getDescription());
        assertEquals(statute.getSportsclubName(), response.getSportsclubName());

    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateStatuteWhenSportsclubNotExists() {
        signIn("superuser", "password");
        StatuteDto statute = StatuteDto.builder()
                .sportsclubName("notExistingSportsclub")
                .title("title2")
                .description("description2")
                .build();

        ResponseEntity<List> addStatuteResponse = restTemplate.postForEntity(ADMIN_CONSOLE_STATUTE, statute, List.class);

        assertEquals(addStatuteResponse.getStatusCode(), HttpStatus.CONFLICT);

        List responseBody = addStatuteResponse.getBody();
        assertField("sportsclubName", ErrorCode.NOT_EXISTS.getCode(), responseBody);
    }

    private CreateSportsclubCommand createSportsclub() {
        CreateSportsclubCommand command = CreateSportsclubCommand.builder()
                .name("name1")
                .description("description")
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .build();
        commandGateway.sendAndWait(command);
        return command;
    }

    private UpdateStatuteCommand createStatute(CreateSportsclubCommand createSportsclubCommand) {
        UUID sportsclubId = sportsclubRepository.findByName(createSportsclubCommand.getName()).get().getId();

        UpdateStatuteCommand command = UpdateStatuteCommand.builder()
                .sportsclubId(sportsclubId)
                .title("title1")
                .description("description1")
                .build();
        commandGateway.sendAndWait(command);
        return command;
    }
}
