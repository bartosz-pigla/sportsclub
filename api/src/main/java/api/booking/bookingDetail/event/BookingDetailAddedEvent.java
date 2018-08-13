package api.booking.bookingDetail.event;

import java.io.Serializable;
import java.util.UUID;

import api.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BookingDetailAddedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -5415447627129094071L;

    private UUID bookingDetailId;
    private UUID sportObjectPositionId;
    private UUID openingTimeId;
}
