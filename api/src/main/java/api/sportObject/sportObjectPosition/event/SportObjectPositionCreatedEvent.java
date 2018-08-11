package api.sportObject.sportObjectPosition.event;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embedded;

import api.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.embeddable.PositionsCount;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class SportObjectPositionCreatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -7590982821936492757L;

    private UUID sportObjectPositionId;
    private UUID sportObjectId;
    private String name;
    private String description;
    @Embedded
    private PositionsCount positionsCount;
}
