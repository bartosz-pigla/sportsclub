package web.common.booking;

import java.util.List;

import com.google.common.collect.ImmutableList;
import commons.ErrorCode;
import domain.booking.exception.CustomerNotExistsException;
import domain.booking.exception.IllegalBookingStateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import web.common.BaseController;
import web.common.dto.FieldErrorDto;
import web.customerApi.booking.dto.BookingDetailDto;

@RestController
public class BookingBaseController extends BaseController {

    protected boolean containsInvalidUUID(BookingDetailDto bookingDetail) {
        return isInvalidUUID(bookingDetail.getOpeningTimeId()) || isInvalidUUID(bookingDetail.getSportObjectPositionId());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerNotExistsException.class)
    public List<FieldErrorDto> handleCustomerNotExistsConflict() {
        return ImmutableList.of(new FieldErrorDto("userId", ErrorCode.NOT_EXISTS));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IllegalBookingStateException.class)
    public List<FieldErrorDto> handleIllegalBookingStateConflict(IllegalBookingStateException e) {
        return ImmutableList.of(new FieldErrorDto("state", ErrorCode.INVALID));
    }
}
