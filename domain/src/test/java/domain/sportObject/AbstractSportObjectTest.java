package domain.sportObject;

import java.util.UUID;

import api.sportObject.event.SportObjectCreatedEvent;
import api.sportsclub.event.SportsclubCreatedEvent;
import domain.sportObject.service.OpeningTimeValidator;
import domain.sportObject.service.SportObjectValidator;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.repository.SportObjectEntityRepository;
import query.repository.SportsclubEntityRepository;

@RunWith(MockitoJUnitRunner.class)
abstract class AbstractSportObjectTest {

    protected AggregateTestFixture<SportObject> testFixture;
    @Mock
    protected SportObjectEntityRepository sportObjectRepository;
    @Mock
    protected SportsclubEntityRepository sportsclubRepository;

    protected static SportsclubCreatedEvent sportsclubCreatedEvent = SportsclubCreatedEvent.builder()
            .sportsclubId(UUID.randomUUID())
            .name("name1")
            .description("description1")
            .build();

    protected static SportObjectCreatedEvent sportObjectCreatedEvent = SportObjectCreatedEvent.builder()
            .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
            .description("description1")
            .name("name1")
            .sportObjectId(UUID.randomUUID())
            .sportsclubId(sportsclubCreatedEvent.getSportsclubId())
            .build();

    @Before
    public void setUp() {
        testFixture = new AggregateTestFixture<>(SportObject.class);
        testFixture.setReportIllegalStateChange(false);
        testFixture.registerInjectableResource(new SportObjectValidator(sportObjectRepository, sportsclubRepository));
        testFixture.registerInjectableResource(new OpeningTimeValidator());
    }
}
