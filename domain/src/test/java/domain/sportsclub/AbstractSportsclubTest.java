package domain.sportsclub;

import domain.sportsclub.service.AnnouncementValidator;
import domain.sportsclub.service.CreateSportsclubValidator;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import query.model.sportsclub.repository.SportsclubEntityRepository;

@RunWith(MockitoJUnitRunner.class)
abstract class AbstractSportsclubTest {

    protected AggregateTestFixture<Sportsclub> testFixture;
    @Mock
    protected SportsclubEntityRepository sportsclubRepository;

    @Before
    public void setUp() {
        testFixture = new AggregateTestFixture<>(Sportsclub.class);
        testFixture.setReportIllegalStateChange(false);
        testFixture.registerInjectableResource(new CreateSportsclubValidator(sportsclubRepository));
        testFixture.registerInjectableResource(new AnnouncementValidator());
        testFixture.registerInjectableResource(sportsclubRepository);
    }
}
