package sportsclub.query.room.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportsclub.query.room.model.RoomEntry;

@Repository
public interface RoomEntryRepository extends JpaRepository<RoomEntry, String> {
}
