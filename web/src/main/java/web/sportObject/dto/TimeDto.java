package web.sportObject.dto;

import java.time.LocalTime;

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
public final class TimeDto {

    private int hour;
    private int minute;

    public static TimeDto create(LocalTime time) {
        return new TimeDto(time.getHour(), time.getMinute());
    }
}
