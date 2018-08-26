package integrationTest.adminApi.sportsclub;

import java.util.UUID;

import api.sportsclub.command.CreateAnnouncementCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import integrationTest.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import query.model.announcement.repository.AnnouncementEntityRepository;
import query.model.announcement.repository.AnnouncementQueryExpressions;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import query.model.sportsclub.repository.SportsclubQueryExpressions;

public abstract class AbstractSportsclubItTest extends IntegrationTest {

    @Autowired
    protected SportsclubEntityRepository sportsclubRepository;
    @Autowired
    protected AnnouncementEntityRepository announcementRepository;

    protected UUID createSportsclub() {
        String name = "name1";

        CreateSportsclubCommand command = CreateSportsclubCommand.builder()
                .name(name)
                .description("description")
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .build();

        commandGateway.sendAndWait(command);

        return sportsclubRepository.findOne(
                SportsclubQueryExpressions.nameMatches(name)).get().getId();
    }

    protected UUID createAnnouncement(UUID sportsclubId) {
        String title = "title1";

        CreateAnnouncementCommand command = CreateAnnouncementCommand.builder()
                .sportsclubId(sportsclubId)
                .title(title)
                .content("content1")
                .build();

        commandGateway.sendAndWait(command);

        return announcementRepository.findOne(
                AnnouncementQueryExpressions.titleMatches(title)).get().getId();
    }
}
