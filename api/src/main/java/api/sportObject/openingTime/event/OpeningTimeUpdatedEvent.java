package api.sportObject.openingTime.event;

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
public final class OpeningTimeUpdatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 2585100464875296765L;

    private UUID openingTimeId;
    private OpeningTimeRange dateRange;
    private Price price;
}
