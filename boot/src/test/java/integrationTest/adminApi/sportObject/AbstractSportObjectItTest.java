package integrationTest.adminApi.sportObject;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.openingTime.command.CreateOpeningTimeCommand;
import api.sportObject.sportObjectPosition.command.CreateSportObjectPositionCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import integrationTest.adminApi.sportsclub.AbstractSportsclubItTest;
import org.springframework.beans.factory.annotation.Autowired;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.PositionsCount;
import query.model.embeddable.Price;
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import query.model.sportobject.repository.SportObjectEntityRepository;
import query.model.sportobject.repository.SportObjectPositionEntityRepository;
import query.model.sportsclub.repository.SportsclubQueryExpressions;
import web.adminApi.sportObject.dto.OpeningTimeDto;
import web.adminApi.sportObject.dto.OpeningTimeRangeDto;

public abstract class AbstractSportObjectItTest extends AbstractSportsclubItTest {

    @Autowired
    protected SportObjectEntityRepository sportObjectRepository;
    @Autowired
    protected OpeningTimeEntityRepository openingTimeRepository;
    @Autowired
    protected SportObjectPositionEntityRepository sportObjectPositionRepository;

    protected CreateSportObjectCommand createSportObject(CreateSportsclubCommand createSportsclubCommand) throws MalformedURLException {
        UUID sportsclubId = sportsclubRepository.findOne(
                SportsclubQueryExpressions.nameMatches(createSportsclubCommand.getName())).get().getId();

        CreateSportObjectCommand createSportObjectCommand = CreateSportObjectCommand.builder()
                .sportsclubId(sportsclubId)
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .description("description1")
                .image(new URL("https://www.w3schools.com/w3css/img_lights.jpg"))
                .name("name1").build();
        commandGateway.sendAndWait(createSportObjectCommand);
        return createSportObjectCommand;
    }

    protected CreateOpeningTimeCommand createOpeningTime(UUID sportObjectId) {
        CreateOpeningTimeCommand command = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectId)
                .price(new Price(new BigDecimal(12.23d)))
                .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0)))
                .build();
        commandGateway.sendAndWait(command);
        return command;
    }

    protected CreateSportObjectPositionCommand createSportObjectPosition(UUID sportObjectId) {
        CreateSportObjectPositionCommand command = CreateSportObjectPositionCommand.builder()
                .sportObjectId(sportObjectId)
                .name("name1")
                .description("description1")
                .positionsCount(new PositionsCount(11))
                .build();
        commandGateway.sendAndWait(command);
        return command;
    }

    protected boolean exists(OpeningTimeRangeDto openingTimeRangeDto) {
        return openingTimeRepository.findAll().stream().anyMatch(o -> {
            OpeningTimeRange timeRange = o.getDateRange();
            OpeningTimeDto startTime = openingTimeRangeDto.getStartTime();
            OpeningTimeDto finishTime = openingTimeRangeDto.getFinishTime();
            return timeRange.getDayOfWeek().equals(openingTimeRangeDto.getDayOfWeek())
                    && timeRange.getStartTime().equals(LocalTime.of(startTime.getHour(), startTime.getMinute()))
                    && timeRange.getFinishTime().equals(LocalTime.of(finishTime.getHour(), finishTime.getMinute()));
        });
    }
}
