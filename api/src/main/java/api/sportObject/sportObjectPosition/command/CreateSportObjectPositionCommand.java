package api.sportObject.sportObjectPosition.command;

import java.util.UUID;

import javax.persistence.Embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import query.model.embeddable.PositiveNumber;

@Data
@AllArgsConstructor
@Builder
public final class CreateSportObjectPositionCommand {

    @TargetAggregateIdentifier
    private UUID sportObjectId;
    private String name;
    private String description;
    @Embedded
    private PositiveNumber positionsCount;
}
