package api.sportObject.sportObjectPosition.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import query.model.embeddable.PositionsCount;

@Data
@AllArgsConstructor
@Builder
public final class UpdateSportObjectPositionCommand {

    @TargetAggregateIdentifier
    private UUID sportObjectId;
    private UUID sportObjectPositionId;
    private String name;
    private String description;
    private PositionsCount positionsCount;

}
