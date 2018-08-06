package web.adminApi.sportObject.dto;

import java.time.LocalTime;

import query.model.embeddable.OpeningTimeRange;
import query.model.sportobject.OpeningTimeEntity;

public final class OpeningTimeRangeDtoFactory {

    public static OpeningTimeRangeDto create(OpeningTimeEntity openingTimeEntity) {
        OpeningTimeRange dateRange = openingTimeEntity.getDateRange();
        LocalTime startTime = dateRange.getStartTime();
        LocalTime finishTime = dateRange.getFinishTime();

        return OpeningTimeRangeDto.builder()
                .id(openingTimeEntity.getId().toString())
                .price(openingTimeEntity.getPrice().getPrice().doubleValue())
                .dayOfWeek(dateRange.getDayOfWeek())
                .startTime(new OpeningTimeDto(startTime.getHour(), startTime.getMinute()))
                .finishTime(new OpeningTimeDto(finishTime.getHour(), finishTime.getMinute()))
                .build();
    }
}
