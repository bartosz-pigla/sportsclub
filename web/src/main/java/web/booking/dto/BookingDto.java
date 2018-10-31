package web.booking.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.booking.BookingState;
import web.user.dto.UserDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public final class BookingDto {

    private String id;
    private String customerId;
    private LocalDateTime date;
    private BookingState state;
    private UserDto customer;
}
