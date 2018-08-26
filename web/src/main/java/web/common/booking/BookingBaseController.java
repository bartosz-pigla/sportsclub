package web.common.booking;

import java.util.List;

import commons.ErrorCode;
import domain.booking.exception.CustomerNotExistsException;
import domain.booking.exception.IllegalBookingStateException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import query.model.booking.repository.BookingEntityRepository;
import web.common.BaseController;
import web.common.dto.FieldErrorDto;
import web.customerApi.booking.dto.BookingDetailDto;

@Setter(onMethod_ = { @Autowired })
public abstract class BookingBaseController extends BaseController {

    protected BookingEntityRepository bookingRepository;

    protected boolean containsInvalidUUID(BookingDetailDto bookingDetail) {
        return isInvalidUUID(bookingDetail.getOpeningTimeId()) || isInvalidUUID(bookingDetail.getSportObjectPositionId());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerNotExistsException.class)
    public List<FieldErrorDto> handleCustomerNotExistsConflict() {
        return errorResponseService.createBody("userId", ErrorCode.NOT_EXISTS);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IllegalBookingStateException.class)
    public List<FieldErrorDto> handleIllegalBookingStateConflict(IllegalBookingStateException e) {
        return errorResponseService.createBody("state", ErrorCode.INVALID);
    }
}
