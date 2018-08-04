package api.sportObject.event;

import java.io.Serializable;
import java.util.UUID;

import api.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.Price;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class OpeningTimeCreatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -5904813871042691401L;

    private UUID sportObjectId;
    private UUID openingTimeId;
    private OpeningTimeRange dateRange;
    private Price price;
}
