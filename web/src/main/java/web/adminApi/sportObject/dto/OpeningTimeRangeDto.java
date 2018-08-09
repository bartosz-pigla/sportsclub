package web.adminApi.sportObject.dto;

import java.time.DayOfWeek;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public final class OpeningTimeRangeDto {

    private String id;
    private DayOfWeek dayOfWeek;
    private OpeningTimeDto startTime;
    private OpeningTimeDto finishTime;
    private Double price;
}