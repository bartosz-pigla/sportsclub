package query.model.sportobject;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.BaseEntity;
import query.model.embeddable.DateRange;
import query.model.embeddable.Price;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OpeningHoursEntity extends BaseEntity {

    @Embedded
    private DateRange dateRange;
    @Embedded
    private Price price;
    @ManyToOne(fetch = FetchType.LAZY)
    private SportObjectEntity sportObject;
}
