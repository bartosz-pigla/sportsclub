package sportsclub.query.booking.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportsclub.query.booking.model.BookingEntry;

@Repository
public interface BookingEntryRepository extends JpaRepository<BookingEntry, String> {
}
