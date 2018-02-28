package sportsclub.query.place.model;

import lombok.*;
import sportsclub.query.room.model.RoomEntry;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(of = {"placeId"})
@Builder
public class PlaceEntry {
    @Id
    private String placeId;

    @Setter
    private RoomEntry room;
}
