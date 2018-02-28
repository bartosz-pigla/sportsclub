package sportsclub.query.booking.model;

import lombok.*;
import sportsclub.query.place.model.PlaceEntry;
import sportsclub.query.room.model.RoomEntry;
import sportsclub.query.user.model.UserEntry;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(of = {"bookingId"})
@Builder
public class BookingEntry {
    @Id
    private String bookingId;

    @Setter
    private UserEntry user;

    @Setter
    private RoomEntry room;

    @Setter
    private Set<PlaceEntry> places;

    @Setter
    private Instant bookingCreationTime;

    @Setter
    private Instant bookingTime;
}
