package api.sportObject.event;

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
public final class OpeningTimeDeletedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 2387782507627207929L;

    private UUID sportObjectId;
    private UUID openingTimeId;
}
