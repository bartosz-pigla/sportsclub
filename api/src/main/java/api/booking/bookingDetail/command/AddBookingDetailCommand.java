package api.booking.bookingDetail.command;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import query.model.sportobject.OpeningTimeEntity;
import query.model.sportobject.SportObjectPositionEntity;

@Data
@AllArgsConstructor
@Builder
public final class AddBookingDetailCommand {

    @TargetAggregateIdentifier
    private UUID bookingId;
    private LocalDate date;
    private SportObjectPositionEntity position;
    private OpeningTimeEntity openingTime;
}
