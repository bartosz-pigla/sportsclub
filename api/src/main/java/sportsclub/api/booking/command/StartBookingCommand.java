package sportsclub.api.booking.command;

import java.time.Instant;
import java.util.Set;

import lombok.Data;

@Data
public class StartBookingCommand {
    String bookingId;
    String userId;
    String roomId;
    Set<String> places;
    Instant bookingCreationTime;
    Instant bookingTime;
}
