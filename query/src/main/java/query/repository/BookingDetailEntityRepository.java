package query.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import query.model.booking.BookingDetailEntity;

public interface BookingDetailEntityRepository extends JpaRepository<BookingDetailEntity, UUID>, QuerydslPredicateExecutor {


}
