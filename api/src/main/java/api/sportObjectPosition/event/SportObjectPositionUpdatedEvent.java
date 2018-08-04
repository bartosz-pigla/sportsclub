package api.sportObjectPosition.event;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embedded;

import api.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.embeddable.PositiveNumber;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class SportObjectPositionUpdatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -1593723441200457728L;

    private UUID sportObjectPositionId;
    private String name;
    private String description;
    @Embedded
    private PositiveNumber positionsCount;
}
