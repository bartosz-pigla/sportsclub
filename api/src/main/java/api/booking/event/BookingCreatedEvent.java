package api.booking.event;

import java.io.Serializable;
import java.time.LocalDateTime;
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
public final class BookingCreatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 8264174054845206460L;

    private UUID bookingId;
    private UUID customerId;
    private LocalDateTime bookingDate;
}
