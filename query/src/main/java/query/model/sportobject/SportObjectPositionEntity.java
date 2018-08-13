package query.model.sportobject;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.baseEntity.BaseEntity;
import query.model.embeddable.PositionsCount;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SportObjectPositionEntity extends BaseEntity {

    private String name;
    private String description;
    @Embedded
    private PositionsCount positionsCount;
    @ManyToOne(fetch = FetchType.LAZY)
    private SportObjectEntity sportObject;
}
