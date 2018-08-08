package api.booking.bookingDetail.event;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import api.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.sportobject.OpeningTimeEntity;
import query.model.sportobject.SportObjectPositionEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BookingDetailAddedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -5415447627129094071L;

    private UUID bookingDetailId;
    private LocalDate date;
    private SportObjectPositionEntity position;
    private OpeningTimeEntity openingTime;
}
