package domain.booking;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingDetail {

}
