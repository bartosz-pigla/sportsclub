package integrationTest.adminApi.sportsclub;

import java.util.UUID;

import api.sportsclub.command.CreateAnnouncementCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import integrationTest.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.repository.SportsclubEntityRepository;

public abstract class AbstractSportsclubItTest extends IntegrationTest {

    @Autowired
    protected SportsclubEntityRepository sportsclubRepository;

    protected CreateSportsclubCommand createSportsclub() {
        CreateSportsclubCommand command = CreateSportsclubCommand.builder()
                .name("name1")
                .description("description")
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .build();
        commandGateway.sendAndWait(command);
        return command;
    }

    protected CreateAnnouncementCommand createAnnouncement(CreateSportsclubCommand createSportsclubCommand) {
        UUID sportsclubId = sportsclubRepository.findByName(createSportsclubCommand.getName()).get().getId();
        CreateAnnouncementCommand command = CreateAnnouncementCommand.builder()
                .sportsclubId(sportsclubId)
                .title("title1")
                .content("content1")
                .build();
        commandGateway.sendAndWait(command);
        return command;
    }
}
