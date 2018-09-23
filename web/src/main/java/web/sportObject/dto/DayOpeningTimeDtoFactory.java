package web.sportObject.dto;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.format.TextStyle;
import java.util.Locale;

import query.model.sportobject.OpeningTimeEntity;

public final class DayOpeningTimeDtoFactory {

    public static DayOpeningTimeDto create(DayOfWeek day, OpeningTimeEntity firstOpeningTime, OpeningTimeEntity lastOpeningTime) {
        return DayOpeningTimeDto.builder()
                .dayOfWeek(day.getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                .startTime(TimeDto.builder()
                        .hour(firstOpeningTime.getTimeRange().getStartTime().getHour())
                        .minute(firstOpeningTime.getTimeRange().getStartTime().getMinute())
                        .build())
                .finishTime(TimeDto.builder()
                        .hour(lastOpeningTime.getTimeRange().getFinishTime().getHour())
                        .minute(lastOpeningTime.getTimeRange().getFinishTime().getMinute())
                        .build())
                .price(firstOpeningTime.getPrice().getPrice().doubleValue())
                .timeInterval((int) Duration.between(
                        firstOpeningTime.getTimeRange().getStartTime(),
                        firstOpeningTime.getTimeRange().getFinishTime()).toMinutes())
                .build();
    }
}
