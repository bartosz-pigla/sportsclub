package web.sportObject.dto;

import java.time.LocalTime;

import query.model.embeddable.OpeningTimeRange;
import query.model.sportobject.OpeningTimeEntity;

public final class OpeningTimeDtoFactory {

    public static OpeningTimeDto create(OpeningTimeEntity openingTime) {
        OpeningTimeRange dateRange = openingTime.getTimeRange();
        LocalTime startTime = dateRange.getStartTime();
        LocalTime finishTime = dateRange.getFinishTime();

        return OpeningTimeDto.builder()
                .id(openingTime.getId().toString())
                .price(openingTime.getPrice().getPrice().doubleValue())
                .dayOfWeek(dateRange.getDayOfWeek())
                .startTime(new TimeDto(startTime.getHour(), startTime.getMinute()))
                .finishTime(new TimeDto(finishTime.getHour(), finishTime.getMinute()))
                .build();
    }
}
