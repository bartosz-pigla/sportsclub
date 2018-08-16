package query.model.booking;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.baseEntity.BaseEntity;
import query.model.sportobject.OpeningTimeEntity;
import query.model.sportobject.SportObjectPositionEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BookingDetailEntity extends BaseEntity {

    private LocalDate date;
    @OneToOne
    private SportObjectPositionEntity position;
    @OneToOne
    private OpeningTimeEntity openingTime;
    @ManyToOne
    private BookingEntity booking;
}
