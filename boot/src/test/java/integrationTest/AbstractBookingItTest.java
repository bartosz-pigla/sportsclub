package integrationTest;

import static query.model.sportobject.repository.OpeningTimeQueryExpressions.sportObjectIdAndTimeRangeMatches;
import static query.model.sportobject.repository.SportObjectQueryExpressions.nameMatches;
import static query.model.user.repository.UserQueryExpressions.usernameMatches;

import java.math.BigDecimal;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.openingTime.command.CreateOpeningTimeCommand;
import api.sportObject.sportObjectPosition.command.CreateSportObjectPositionCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import query.model.booking.repository.BookingDetailEntityRepository;
import query.model.booking.repository.BookingEntityRepository;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.PositionsCount;
import query.model.embeddable.Price;
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import query.model.sportobject.repository.SportObjectEntityRepository;
import query.model.sportobject.repository.SportObjectPositionEntityRepository;
import query.model.sportobject.repository.SportObjectPositionQueryExpressions;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import query.model.sportsclub.repository.SportsclubQueryExpressions;
import query.model.user.repository.UserEntityRepository;

public abstract class AbstractBookingItTest extends IntegrationTest {

    @Autowired
    protected BookingEntityRepository bookingRepository;
    @Autowired
    protected BookingDetailEntityRepository bookingDetailRepository;
    @Autowired
    protected SportsclubEntityRepository sportsclubRepository;
    @Autowired
    protected SportObjectEntityRepository sportObjectRepository;
    @Autowired
    protected SportObjectPositionEntityRepository sportObjectPositionRepository;
    @Autowired
    protected OpeningTimeEntityRepository openingTimeRepository;
    @Autowired
    protected UserEntityRepository userRepository;

    protected UUID sportsclubId;
    protected UUID sportObjectId;
    protected UUID sportObjectPositionId;
    protected UUID openingTimeId;
    protected UUID customerId;

    @Before
    public void setUp() {
        super.setUp();
        sportsclubId = createSportsclub();
        sportObjectId = createSportObject(sportsclubId);
        sportObjectPositionId = createSportObjectPosition(sportObjectId);
        openingTimeId = createOpeningTime(sportObjectId);
        customerId = userRepository.findOne(usernameMatches("customer")).get().getId();
    }

    protected UUID createSportsclub() {
        CreateSportsclubCommand command = CreateSportsclubCommand.builder()
                .name("name1")
                .description("description")
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .build();
        commandGateway.sendAndWait(command);
        return sportsclubRepository.findOne(SportsclubQueryExpressions.nameMatches(command.getName())).get().getId();
    }

    protected UUID createSportObject(UUID sportsclubId) {
        try {
            CreateSportObjectCommand createSportObjectCommand = CreateSportObjectCommand.builder()
                    .sportsclubId(sportsclubId)
                    .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                    .description("description1")
                    .image(new URL("https://www.w3schools.com/w3css/img_lights.jpg"))
                    .name("name1").build();
            commandGateway.sendAndWait(createSportObjectCommand);
            return sportObjectRepository.findOne(nameMatches(createSportObjectCommand.getName())).get().getId();
        } catch (Exception exc) {
            return UUID.randomUUID();
        }
    }

    protected UUID createSportObjectPosition(UUID sportObjectId) {
        CreateSportObjectPositionCommand command = CreateSportObjectPositionCommand.builder()
                .sportObjectId(sportObjectId)
                .name("name1")
                .description("description1")
                .positionsCount(new PositionsCount(1))
                .build();
        commandGateway.sendAndWait(command);
        return sportObjectPositionRepository.findOne(SportObjectPositionQueryExpressions.nameMatches(command.getName())).get().getId();
    }

    protected UUID createOpeningTime(UUID sportObjectId) {
        CreateOpeningTimeCommand command = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectId)
                .price(new Price(new BigDecimal(12.23d)))
                .timeRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0)))
                .build();
        commandGateway.sendAndWait(command);
        return openingTimeRepository.findOne(sportObjectIdAndTimeRangeMatches(sportObjectId, command.getTimeRange())).get().getId();
    }
}
