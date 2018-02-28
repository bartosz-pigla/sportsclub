package sportsclub.api.booking.event;

import java.time.Instant;
import java.util.Set;

import lombok.Value;

@Value
public class BookingStartedEvent {
    String bookingId;
    String userId;
    String roomId;
    Set<String> places;
    Instant bookingCreationTime;
    Instant bookingTime;
}
