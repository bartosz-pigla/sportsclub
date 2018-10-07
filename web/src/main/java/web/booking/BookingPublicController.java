package web.booking;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static query.model.booking.repository.BookingDetailQueryExpressions.bookingDateAndSportObjectMatchesAndIsInOpeningTimeRange;
import static web.booking.dto.BookingDetailWithOpeningTimeAndPositionDto.createFrom;
import static web.common.RequestMappings.PUBLIC_API_OPENING_TIMES_WITH_BOOKINGS;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import query.model.booking.BookingDetailEntity;
import query.model.booking.repository.BookingDetailEntityRepository;
import query.model.sportobject.PositionsWithOpeningTimes;
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import web.booking.dto.BookingDetailWithOpeningTimeAndPositionDto;

@RestController
@Setter(onMethod_ = { @Autowired })
final class BookingPublicController extends BookingBaseController {

    private OpeningTimeEntityRepository openingTimeRepository;
    private BookingDetailEntityRepository bookingDetailRepository;

    @GetMapping(PUBLIC_API_OPENING_TIMES_WITH_BOOKINGS)
    List<BookingDetailWithOpeningTimeAndPositionDto> getPositionsWithOpeningTimes(@PathVariable UUID sportObjectId,
                                                                                  @RequestParam String dateTime) {
        LocalDateTime date = DateTimeFormatter.RFC_1123_DATE_TIME.parse(dateTime, LocalDateTime::from);

        List<PositionsWithOpeningTimes> positionsWithOpeningTimes = this.openingTimeRepository
                .getPositionsWithOpeningTimes(sportObjectId, date.getDayOfWeek());

        if (positionsWithOpeningTimes.isEmpty()) {
            return positionsWithOpeningTimes.stream().map(p -> createFrom(p, 0)).collect(toList());
        } else {
            List<BookingDetailEntity> bookingDetails = newArrayList(bookingDetailRepository.findAll(bookingDateAndSportObjectMatchesAndIsInOpeningTimeRange(
                    date.toLocalDate(),
                    positionsWithOpeningTimes.get(0).getStartTime(),
                    positionsWithOpeningTimes.get(positionsWithOpeningTimes.size() - 1).getFinishTime(),
                    sportObjectId)));

            return positionsWithOpeningTimes.stream()
                    .map(positionWithOpeningTime -> {
                        int bookedPositionsCount = (int) bookingDetails.stream().filter(b -> b.getOpeningTime().getId().equals(positionWithOpeningTime.getOpeningTimeId()) &&
                                b.getPosition().getId().equals(positionWithOpeningTime.getPositionId())).count();
                        return createFrom(positionWithOpeningTime, bookedPositionsCount);
                    })
                    .collect(toList());
        }
    }
}
