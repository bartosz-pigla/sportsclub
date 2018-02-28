package sportsclub.api.booking.command;

import lombok.Data;
import java.util.Set;

@Data
public class ChangeBookingCommand {
    String bookingId;
    Set<String> places;
}
