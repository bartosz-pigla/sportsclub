package api.user.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@Builder
public final class DeactivateUserCommand {

    @TargetAggregateIdentifier
    private UUID userId;
}
