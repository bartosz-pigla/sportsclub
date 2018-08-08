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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BookingDetailUpdatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 2268581075493923580L;

    private UUID bookingDetailId;
    private LocalDate date;
    private OpeningTimeEntity openingTime;
}
