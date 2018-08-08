package query.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import query.model.booking.BookingEntity;

public interface BookingEntityRepository extends JpaRepository<BookingEntity, UUID> {

}
