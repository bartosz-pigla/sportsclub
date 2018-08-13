package query.model.booking;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.baseEntity.BaseEntity;
import query.model.user.UserEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BookingEntity extends BaseEntity {

    private LocalDateTime bookingDate;
    private BookingState bookingState;
    @OneToMany(fetch = FetchType.LAZY)
    private List<BookingDetailEntity> bookingDetails;
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity customer;
}
