package api.sportObjectPosition.event;

import java.io.Serializable;
import java.util.UUID;

import api.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class SportObjectPositionDeletedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 4504020133523036845L;

    private UUID sportObjectPositionId;
}
