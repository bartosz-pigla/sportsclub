package api.booking.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@Builder
public final class RejectBookingCommand {

    @TargetAggregateIdentifier
    private UUID bookingId;
}
