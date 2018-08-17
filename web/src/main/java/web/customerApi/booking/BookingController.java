package web.customerApi.booking;

import static java.util.UUID.fromString;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_BY_ID;

import java.util.List;
import java.util.UUID;

import api.booking.command.CancelBookingCommand;
import api.booking.command.CreateBookingCommand;
import api.booking.command.SubmitBookingCommand;
import com.google.common.collect.ImmutableList;
import commons.ErrorCode;
import domain.booking.exception.CustomerNotExistsException;
import domain.booking.exception.IllegalBookingStateException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.booking.BookingState;
import web.common.BaseController;
import web.common.dto.FieldErrorDto;
import web.publicApi.signIn.dto.UserPrincipal;

@RestController
@Setter(onMethod_ = { @Autowired })
final class BookingController extends BaseController {

    @PostMapping(CUSTOMER_API_BOOKING)
    ResponseEntity<?> create(@AuthenticationPrincipal UserPrincipal principal) {
        commandGateway.sendAndWait(new CreateBookingCommand(fromString(principal.getId())));
        return noContent().build();
    }

    @PatchMapping(CUSTOMER_API_BOOKING_BY_ID)
    ResponseEntity<?> changeState(@PathVariable String bookingId,
                                  @RequestBody BookingState state,
                                  @AuthenticationPrincipal UserPrincipal principal) {
        if (isInvalidUUID(bookingId)) {
            return badRequest().build();
        }

        return changeState(fromString(bookingId), fromString(principal.getId()), state);
    }

    private ResponseEntity<?> changeState(UUID bookingId, UUID customerId, BookingState state) {
        switch (state) {
            case SUBMITTED:
                commandGateway.sendAndWait(new SubmitBookingCommand(bookingId, customerId));
                return noContent().build();
            case CANCELED:
                commandGateway.sendAndWait(new CancelBookingCommand(bookingId, customerId));
                return noContent().build();
            default:
                return badRequest().build();
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerNotExistsException.class)
    public List<FieldErrorDto> handleCustomerNotExistsConflict() {
        return ImmutableList.of(new FieldErrorDto("userId", ErrorCode.NOT_EXISTS));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IllegalBookingStateException.class)
    public List<FieldErrorDto> handleIllegalBookingStateConflict() {
        return ImmutableList.of(new FieldErrorDto("state", ErrorCode.INVALID));
    }
}
