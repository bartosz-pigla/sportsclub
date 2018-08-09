package api.sportObject.openingTime.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.Price;

@Data
@AllArgsConstructor
@Builder
public final class UpdateOpeningTimeCommand {

    @TargetAggregateIdentifier
    private UUID sportObjectId;
    private UUID openingTimeId;
    private OpeningTimeRange dateRange;
    private Price price;
}
