package sportsclub.api.booking.event;

import lombok.Value;

import java.util.Set;

@Value
public class BookingChangedEvent {
    String bookingId;
    Set<String> places;
}
