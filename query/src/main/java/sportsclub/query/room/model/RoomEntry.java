package sportsclub.query.room.model;

import lombok.*;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(of = {"roomId"})
@Builder
public class RoomEntry {
}
