package query.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import query.model.booking.BookingDetailEntity;

public interface BookingDetailEntityRepository extends JpaRepository<BookingDetailEntity, UUID> {

}
