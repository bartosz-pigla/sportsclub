package web.sportObject.dto;

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
public final class OpeningTimeDto {

    private String id;
    private DayOfWeek dayOfWeek;
    private TimeDto startTime;
    private TimeDto finishTime;
    private Double price;
}
