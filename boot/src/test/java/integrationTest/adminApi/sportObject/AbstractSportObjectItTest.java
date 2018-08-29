package integrationTest.adminApi.sportObject;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.openingTime.command.CreateOpeningTimeCommand;
import api.sportObject.sportObjectPosition.command.CreateSportObjectPositionCommand;
import integrationTest.adminApi.sportsclub.AbstractSportsclubItTest;
import org.springframework.beans.factory.annotation.Autowired;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.model.embeddable.ImageUrl;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.PositionsCount;
import query.model.embeddable.Price;
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import query.model.sportobject.repository.OpeningTimeQueryExpressions;
import query.model.sportobject.repository.SportObjectEntityRepository;
import query.model.sportobject.repository.SportObjectPositionEntityRepository;
import query.model.sportobject.repository.SportObjectPositionQueryExpressions;
import query.model.sportobject.repository.SportObjectQueryExpressions;
import web.sportObject.dto.OpeningTimeDto;
import web.sportObject.dto.TimeDto;

public abstract class AbstractSportObjectItTest extends AbstractSportsclubItTest {

    @Autowired
    protected SportObjectEntityRepository sportObjectRepository;
    @Autowired
    protected OpeningTimeEntityRepository openingTimeRepository;
    @Autowired
    protected SportObjectPositionEntityRepository sportObjectPositionRepository;

    protected UUID createSportObject(UUID sportsclubId) {
        String name = "name1";

        CreateSportObjectCommand createSportObjectCommand = CreateSportObjectCommand.builder()
                .name(name)
                .sportsclubId(sportsclubId)
                .address(new Address("street", new City("Wroclaw"), new Coordinates(0d, 0d)))
                .description("description1")
                .imageUrl(new ImageUrl("https://www.w3schools.com/w3css/img_lights.jpg"))
                .build();

        commandGateway.sendAndWait(createSportObjectCommand);

        return sportObjectRepository.findOne(
                SportObjectQueryExpressions.nameAndSportsclubIdMatches(name, sportsclubId)).get().getId();
    }

    protected UUID createOpeningTime(UUID sportObjectId) {
        OpeningTimeRange timeRange =
                new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0));

        CreateOpeningTimeCommand command = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectId)
                .price(new Price(new BigDecimal(12.23d)))
                .timeRange(timeRange)
                .build();

        commandGateway.sendAndWait(command);

        return openingTimeRepository.findOne(
                OpeningTimeQueryExpressions.sportObjectIdAndTimeRangeMatches(sportObjectId, timeRange)).get().getId();
    }

    protected UUID createSportObjectPosition(UUID sportObjectId) {
        String name = "name1";

        CreateSportObjectPositionCommand command = CreateSportObjectPositionCommand.builder()
                .sportObjectId(sportObjectId)
                .name(name)
                .description("description1")
                .positionsCount(new PositionsCount(11))
                .build();

        commandGateway.sendAndWait(command);

        return sportObjectPositionRepository.findOne(
                SportObjectPositionQueryExpressions.nameMatches(name)).get().getId();
    }

    protected boolean exists(OpeningTimeDto openingTimeDto) {
        return openingTimeRepository.findAll().stream().anyMatch(o -> {
            OpeningTimeRange timeRange = o.getTimeRange();
            TimeDto startTime = openingTimeDto.getStartTime();
            TimeDto finishTime = openingTimeDto.getFinishTime();
            return timeRange.getDayOfWeek().equals(openingTimeDto.getDayOfWeek())
                    && timeRange.getStartTime().equals(LocalTime.of(startTime.getHour(), startTime.getMinute()))
                    && timeRange.getFinishTime().equals(LocalTime.of(finishTime.getHour(), finishTime.getMinute()));
        });
    }
}
