package integrationTest.adminApi.sportObject;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import api.sportObject.command.CreateOpeningTimeCommand;
import api.sportObject.command.CreateSportObjectCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import integrationTest.adminApi.sportsclub.AbstractSportsclubItTest;
import org.springframework.beans.factory.annotation.Autowired;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.Price;
import query.repository.OpeningTimeEntityRepository;
import query.repository.SportObjectEntityRepository;
import web.adminApi.sportObject.dto.OpeningTimeDto;
import web.adminApi.sportObject.dto.OpeningTimeRangeDto;

public abstract class AbstractSportObjectItTest extends AbstractSportsclubItTest {

    @Autowired
    protected SportObjectEntityRepository sportObjectRepository;
    @Autowired
    protected OpeningTimeEntityRepository openingTimeRepository;

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

    protected CreateOpeningTimeCommand createOpeningTime(UUID sportObjectId) {
        CreateOpeningTimeCommand createOpeningTimeCommand = CreateOpeningTimeCommand.builder()
                .sportObjectId(sportObjectId)
                .price(new Price(new BigDecimal(12.23d)))
                .dateRange(new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0)))
                .build();
        commandGateway.sendAndWait(createOpeningTimeCommand);
        return createOpeningTimeCommand;
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
