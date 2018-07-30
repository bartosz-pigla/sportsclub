package api.sportsclub.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@Builder
public final class UpdateAnnouncementCommand {

    @TargetAggregateIdentifier
    private UUID sportsclubId;
    private UUID announcementId;
    private String title;
    private String content;
}
