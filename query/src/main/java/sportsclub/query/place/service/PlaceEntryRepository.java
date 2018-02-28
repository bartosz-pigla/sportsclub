package sportsclub.query.place.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportsclub.query.place.model.PlaceEntry;

@Repository
public interface PlaceEntryRepository extends JpaRepository<PlaceEntry, String> {
}
