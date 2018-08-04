package integrationTest.adminApi.sportObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import integrationTest.adminApi.sportsclub.AbstractSportsclubItTest;
import org.springframework.beans.factory.annotation.Autowired;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.repository.SportObjectEntityRepository;

public abstract class AbstractSportObjectItTest extends AbstractSportsclubItTest {

    @Autowired
    protected SportObjectEntityRepository sportObjectRepository;

    protected CreateSportObjectCommand createSportObject(CreateSportsclubCommand createSportsclubCommand) throws MalformedURLException {
        UUID sportsclubId = sportsclubRepository.findByName(createSportsclubCommand.getName()).get().getId();

        CreateSportObjectCommand createSportObjectCommand = CreateSportObjectCommand.builder()
                .sportsclubId(sportsclubId)
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .description("description1")
                .image(new URL("https://www.w3schools.com/w3css/img_lights.jpg"))
                .name("name1").build();
        commandGateway.sendAndWait(createSportObjectCommand);
        return createSportObjectCommand;
    }
}
