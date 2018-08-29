package boot.populator;

import static query.model.sportsclub.repository.SportsclubQueryExpressions.nameMatches;

import api.sportsclub.command.CreateSportsclubCommand;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.model.sportsclub.repository.SportsclubEntityRepository;

@Service
@AllArgsConstructor
public final class SportsclubPopulator {

    private CommandGateway commandGateway;
    private SportsclubEntityRepository sportsclubRepository;

    public void initializeSportsclub() {
        String name = "sportsclub";

        if (!sportsclubRepository.exists(nameMatches(name))) {
            commandGateway.sendAndWait(CreateSportsclubCommand.builder()
                    .name(name)
                    .description("description")
                    .address(new Address("street", new City("City"), new Coordinates(50d, 50d)))
                    .build());
        }
    }
}
