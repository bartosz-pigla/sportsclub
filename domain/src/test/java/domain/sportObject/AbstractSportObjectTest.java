package domain.sportObject;

import java.util.UUID;

import api.sportObject.event.SportObjectCreatedEvent;
import api.sportObject.sportObjectPosition.event.SportObjectPositionCreatedEvent;
import api.sportsclub.event.SportsclubCreatedEvent;
import domain.sportObject.service.OpeningTimeValidator;
import domain.sportObject.service.SportObjectPositionValidator;
import domain.sportObject.service.SportObjectValidator;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.model.embeddable.PositionsCount;
import query.repository.SportObjectEntityRepository;
import query.repository.SportsclubEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractSportObjectTest {

    protected AggregateTestFixture<SportObject> testFixture;
    @Mock
    protected SportObjectEntityRepository sportObjectRepository;
    @Mock
    protected SportsclubEntityRepository sportsclubRepository;

    protected UUID sportsclubId = UUID.randomUUID();
    protected UUID sportObjectId = UUID.randomUUID();
    protected UUID sportObjectPositionId = UUID.randomUUID();

    protected SportsclubCreatedEvent sportsclubCreatedEvent = SportsclubCreatedEvent.builder()
            .sportsclubId(sportsclubId)
            .name("name1")
            .description("description1")
            .build();

    protected SportObjectCreatedEvent sportObjectCreatedEvent = SportObjectCreatedEvent.builder()
            .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
            .description("description1")
            .name("name1")
            .sportObjectId(sportObjectId)
            .sportsclubId(sportsclubId)
            .build();

    protected SportObjectPositionCreatedEvent sportObjectPositionCreatedEvent = SportObjectPositionCreatedEvent.builder()
            .sportObjectPositionId(sportObjectPositionId)
            .sportObjectId(sportObjectId)
            .name("name1")
            .description("description1")
            .positionsCount(new PositionsCount(11))
            .build();

    @Before
    public void setUp() {
        testFixture = new AggregateTestFixture<>(SportObject.class);
        testFixture.setReportIllegalStateChange(false);
        testFixture.registerInjectableResource(new SportObjectValidator(sportObjectRepository, sportsclubRepository));
        testFixture.registerInjectableResource(new OpeningTimeValidator());
        testFixture.registerInjectableResource(new SportObjectPositionValidator());
    }
}
