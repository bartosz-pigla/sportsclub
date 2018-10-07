package web.booking.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Value;
import query.model.sportobject.PositionsWithOpeningTimes;
import web.sportObject.dto.TimeDto;

@Value
public class BookingDetailWithOpeningTimeAndPositionDto {

    UUID positionId;
    String name;
    Integer bookedPositionsCount;
    Integer positionsCount;
    UUID openingTimeId;
    TimeDto startTime;
    TimeDto finishTime;
    BigDecimal price;

    public static BookingDetailWithOpeningTimeAndPositionDto createFrom(
            PositionsWithOpeningTimes positionsWithOpeningTimes, int bookedPositionsCount) {
        return new BookingDetailWithOpeningTimeAndPositionDto(
                positionsWithOpeningTimes.getPositionId(),
                positionsWithOpeningTimes.getName(),
                bookedPositionsCount,
                positionsWithOpeningTimes.getPositionsCount().getPositionsCount(),
                positionsWithOpeningTimes.getOpeningTimeId(),
                TimeDto.create(positionsWithOpeningTimes.getStartTime()),
                TimeDto.create(positionsWithOpeningTimes.getFinishTime()),
                positionsWithOpeningTimes.getPrice().getPrice());
    }
}
