package api.sportObject.openingTime.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@Builder
public final class DeleteOpeningTimeCommand {

    @TargetAggregateIdentifier
    private UUID sportObjectId;
    private UUID openingTimeId;
}
