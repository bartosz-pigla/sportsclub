package web.customerApi.booking.dto;

import java.time.LocalDate;

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
public final class BookingDetailDto {

    private String bookingDetailId;
    private String sportObjectPositionId;
    private String openingTimeId;
    private LocalDate date;
}
