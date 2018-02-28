package sportsclub.api.booking.command;

import lombok.Data;

@Data
public class CancelBookingCommand {
    String bookingId;
}
