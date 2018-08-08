package api.booking.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public final class CreateBookingCommand {

    private UUID customerId;
}
