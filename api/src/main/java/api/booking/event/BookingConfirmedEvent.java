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
public final class BookingConfirmedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 7885081898852365344L;

    private UUID bookingId;
}
