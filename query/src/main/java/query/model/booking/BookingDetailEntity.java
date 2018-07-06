package query.model.booking;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.BaseEntity;
import query.model.embeddable.DateTimeRange;
import query.model.sportobject.SportObjectPositionEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BookingDetailEntity extends BaseEntity {

    @Embedded
    private DateTimeRange dateTimeRange;
    @OneToOne(fetch = FetchType.LAZY)
    private SportObjectPositionEntity position;
}
