package api.sportsclub.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@Builder
public final class UpdateStatuteCommand {

    @TargetAggregateIdentifier
    private UUID sportsclubId;
    private String title;
    private String description;
}
