package sportsclub.domain.user.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sportsclub.domain.user.model.UserEntry;

@Repository
public interface UserEntryRepository extends JpaRepository<UserEntry, String> {
    @Modifying
    @Query("UPDATE UserEntry u SET u.activated = true WHERE u.login = :login")
    void activate(@Param("login") String login);
}
