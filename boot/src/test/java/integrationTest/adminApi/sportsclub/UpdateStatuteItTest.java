package integrationTest.adminApi.sportsclub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static query.model.sportobject.repository.SportObjectQueryExpressions.nameMatches;
import static web.common.RequestMappings.ADMIN_CONSOLE_STATUTE;

import java.util.UUID;

import api.sportsclub.command.CreateSportsclubCommand;
import api.sportsclub.command.UpdateStatuteCommand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import web.adminApi.sportsclub.dto.StatuteDto;

public final class UpdateStatuteItTest extends AbstractSportsclubItTest {

    @Autowired
    private SportsclubEntityRepository sportsclubRepository;

    @Test
    @DirtiesContext
    public void shouldAddStatuteWhenNotExistsAndSportsclubExists() {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        signIn("superuser", "password");
        StatuteDto statute = StatuteDto.builder()
                .title("title1")
                .description("description1")
                .build();

        ResponseEntity<StatuteDto> addStatuteResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_STATUTE,
                statute,
                StatuteDto.class,
                createSportsclubCommand.getName());
        assertEquals(addStatuteResponse.getStatusCode(), HttpStatus.OK);

        StatuteDto response = addStatuteResponse.getBody();
        assertEquals(statute.getTitle(), response.getTitle());
        assertEquals(statute.getDescription(), response.getDescription());
        assertNotNull(sportsclubRepository.findOne(nameMatches(createSportsclubCommand.getName())).get().getStatute());
    }

    @Test
    @DirtiesContext
    public void shouldUpdateStatuteWhenExistsAndSportsclubExists() {
        CreateSportsclubCommand createSportsclubCommand = createSportsclub();
        UpdateStatuteCommand updateStatuteCommand = createStatute(createSportsclubCommand);
        signIn("superuser", "password");
        StatuteDto statute = StatuteDto.builder()
                .title("title2")
                .description("description2")
                .build();

        ResponseEntity<StatuteDto> addStatuteResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_STATUTE,
                statute,
                StatuteDto.class,
                createSportsclubCommand.getName());

        assertEquals(addStatuteResponse.getStatusCode(), HttpStatus.OK);

        StatuteDto response = addStatuteResponse.getBody();
        assertEquals(statute.getTitle(), response.getTitle());
        assertEquals(statute.getDescription(), response.getDescription());
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateStatuteWhenSportsclubNotExists() {
        signIn("superuser", "password");
        StatuteDto statute = StatuteDto.builder()
                .title("title2")
                .description("description2")
                .build();

        ResponseEntity addStatuteResponse = restTemplate.postForEntity(
                ADMIN_CONSOLE_STATUTE,
                statute,
                null,
                "notExistingSportsclub");

        assertEquals(addStatuteResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    private UpdateStatuteCommand createStatute(CreateSportsclubCommand createSportsclubCommand) {
        UUID sportsclubId = sportsclubRepository.findOne(nameMatches(createSportsclubCommand.getName())).get().getId();

        UpdateStatuteCommand command = UpdateStatuteCommand.builder()
                .sportsclubId(sportsclubId)
                .title("title1")
                .description("description1")
                .build();
        commandGateway.sendAndWait(command);
        return command;
    }
}
