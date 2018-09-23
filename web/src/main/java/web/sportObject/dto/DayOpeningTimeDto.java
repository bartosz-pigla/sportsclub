package web.sportObject.dto;

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
public final class DayOpeningTimeDto {

    private String dayOfWeek;
    private TimeDto startTime;
    private TimeDto finishTime;
    private int timeInterval;
    private Double price;
}
