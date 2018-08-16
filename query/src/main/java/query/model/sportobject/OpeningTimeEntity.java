package query.model.sportobject;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.baseEntity.BaseEntity;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.Price;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OpeningTimeEntity extends BaseEntity {

    @Embedded
    private OpeningTimeRange timeRange;
    @Embedded
    private Price price;
    @ManyToOne(fetch = FetchType.LAZY)
    private SportObjectEntity sportObject;
}
