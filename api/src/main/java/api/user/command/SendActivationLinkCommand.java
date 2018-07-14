package api.user.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@Builder
public class SendActivationLinkCommand {

    @TargetAggregateIdentifier
    private UUID customerId;
}
