package api.sportsclub.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
public final class RemoveAnnouncementCommand {

    @TargetAggregateIdentifier
    private UUID sportsclubId;
    private UUID announcementId;
}
