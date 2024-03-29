package api.booking.event;

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
public final class BookingRejectedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 1634458697956016693L;

    private UUID bookingId;
}
