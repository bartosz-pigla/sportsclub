package api.sportsclub.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@Builder
public final class ChangeAnnouncementCommand {

    @TargetAggregateIdentifier
    private UUID sportsclubId;
    private UUID announcementId;
    private String title;
    private String content;
}
