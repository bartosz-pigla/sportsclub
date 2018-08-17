package api.booking.bookingDetail.command;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@Builder
public final class AddBookingDetailCommand {

    @TargetAggregateIdentifier
    private UUID bookingId;
    private UUID customerId;
    private UUID sportObjectPositionId;
    private UUID openingTimeId;
    private LocalDate date;
}
