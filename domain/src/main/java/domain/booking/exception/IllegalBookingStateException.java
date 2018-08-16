package domain.booking.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import query.model.booking.BookingState;

@Getter
@AllArgsConstructor
public final class IllegalBookingStateException extends RuntimeException {

    private BookingState currentState;
    private BookingState targetState;
}
