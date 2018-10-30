package web.booking.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public final class BookingDetailDto {

    private String bookingDetailId;
    private String sportObjectPositionId;
    private String openingTimeId;

    @DateTimeFormat(pattern = "EEE MMM dd yyyy")
    @JsonFormat(pattern = "EEE MMM dd yyyy")
    private LocalDate date;
}
