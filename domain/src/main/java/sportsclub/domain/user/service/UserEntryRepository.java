package sportsclub.domain.user.service;

import org.springframework.data.jpa.repository.JpaRepository;
import sportsclub.domain.user.model.UserEntry;

public interface UserEntryRepository extends JpaRepository<UserEntry, String> {
}
