package integrationTest.adminApi.sportsclub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static query.model.sportsclub.repository.SportsclubQueryExpressions.idMatches;
import static web.common.RequestMappings.DIRECTOR_API_STATUTE;

import java.util.UUID;

import api.sportsclub.command.UpdateStatuteCommand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import query.model.sportsclub.repository.StatuteEntityRepository;
import query.model.sportsclub.repository.StatuteQueryExpressions;
import web.adminApi.sportsclub.dto.StatuteDto;

public final class UpdateStatuteItTest extends AbstractSportsclubItTest {

    @Autowired
    private SportsclubEntityRepository sportsclubRepository;
    @Autowired
    private StatuteEntityRepository statuteRepository;

    @Test
    @DirtiesContext
    public void shouldAddStatuteWhenNotExistsAndSportsclubExists() {
        UUID sportsclubId = createSportsclub();
        signIn("superuser", "password");

        StatuteDto statute = StatuteDto.builder()
                .title("title1")
                .description("description1")
                .build();

        ResponseEntity<StatuteDto> addStatuteResponse = restTemplate.postForEntity(
                DIRECTOR_API_STATUTE,
                statute,
                StatuteDto.class,
                sportsclubId.toString());

        assertEquals(addStatuteResponse.getStatusCode(), HttpStatus.OK);
        assertNotNull(sportsclubRepository.findOne(idMatches(sportsclubId)).get().getStatute());
    }

    @Test
    @DirtiesContext
    public void shouldUpdateStatuteWhenExistsAndSportsclubExists() {
        UUID sportsclubId = createSportsclub();
        createStatute(sportsclubId);
        signIn("superuser", "password");

        String title = "title2";
        StatuteDto statute = StatuteDto.builder()
                .title(title)
                .description("description2")
                .build();

        ResponseEntity<StatuteDto> addStatuteResponse = restTemplate.postForEntity(
                DIRECTOR_API_STATUTE,
                statute,
                StatuteDto.class,
                sportsclubId.toString());

        assertEquals(addStatuteResponse.getStatusCode(), HttpStatus.OK);

        assertTrue(statuteRepository.exists(
                StatuteQueryExpressions.titleAndSportsclubIdMatches(title, sportsclubId)));
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
                DIRECTOR_API_STATUTE,
                statute,
                null,
                "notExistingSportsclub");

        assertEquals(addStatuteResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    private UUID createStatute(UUID sportsclubId) {
        String title = "title1";

        UpdateStatuteCommand command = UpdateStatuteCommand.builder()
                .sportsclubId(sportsclubId)
                .title(title)
                .description("description1")
                .build();

        commandGateway.sendAndWait(command);

        return statuteRepository.findOne(
                StatuteQueryExpressions.titleAndSportsclubIdMatches(title, sportsclubId)).get().getId();
    }
}
