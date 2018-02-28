package sportsclub.api.booking.event;

import lombok.Value;

@Value
public class BookingConfirmedEvent {
    String bookingId;
}
