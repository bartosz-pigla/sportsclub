package api.booking.bookingDetail.command;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import query.model.sportobject.OpeningTimeEntity;

@Data
@AllArgsConstructor
@Builder
public final class UpdateBookingDetailCommand {

    @TargetAggregateIdentifier
    private UUID bookingId;
    private UUID bookingDetailId;
    private LocalDate date;
    private OpeningTimeEntity openingTime;
}
