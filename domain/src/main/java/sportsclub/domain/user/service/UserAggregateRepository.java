package sportsclub.domain.user.service;

import org.springframework.data.jpa.repository.JpaRepository;
import sportsclub.domain.user.model.User;

public interface UserAggregateRepository extends JpaRepository<User, String> {
}
