package web.customerApi.booking;

import static java.util.UUID.fromString;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_CANCEL;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_DETAIL;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_DETAIL_BY_ID;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_SUBMIT;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import api.booking.bookingDetail.command.DeleteBookingDetailCommand;
import api.booking.command.CancelBookingCommand;
import api.booking.command.CreateBookingCommand;
import api.booking.command.SubmitBookingCommand;
import com.google.common.collect.ImmutableList;
import commons.ErrorCode;
import domain.booking.exception.AllSportObjectPositionsAlreadyBookedException;
import domain.booking.exception.BookingDetailsLimitExceededException;
import domain.booking.exception.InvalidOpeningTimeAssignException;
import domain.common.exception.NotExistsException;
import domain.sportObject.exception.OpeningTimeNotExistsException;
import domain.sportObject.exception.SportObjectPositionNotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import web.common.booking.BookingBaseController;
import web.common.dto.FieldErrorDto;
import web.customerApi.booking.dto.BookingDetailDto;
import web.publicApi.signIn.dto.UserPrincipal;

@RestController
final class CustomerBookingController extends BookingBaseController {

    @PostMapping(CUSTOMER_API_BOOKING)
    ResponseEntity<?> create(@AuthenticationPrincipal UserPrincipal principal) {
        commandGateway.sendAndWait(new CreateBookingCommand(fromString(principal.getId())));
        return noContent().build();
    }

    @PatchMapping(CUSTOMER_API_BOOKING_CANCEL)
    ResponseEntity<?> cancel(@PathVariable String bookingId, @AuthenticationPrincipal UserPrincipal principal) {
        return changeState(bookingId, principal, CancelBookingCommand::new);
    }

    @PatchMapping(CUSTOMER_API_BOOKING_SUBMIT)
    ResponseEntity<?> submit(@PathVariable String bookingId, @AuthenticationPrincipal UserPrincipal principal) {
        return changeState(bookingId, principal, SubmitBookingCommand::new);
    }

    @PostMapping(CUSTOMER_API_BOOKING_DETAIL)
    ResponseEntity<?> addDetail(@PathVariable String bookingId,
                                @RequestBody BookingDetailDto bookingDetail,
                                @AuthenticationPrincipal UserPrincipal principal) {
        if (isInvalidUUID(bookingId) || containsInvalidUUID(bookingDetail)) {
            return badRequest().build();
        }

        commandGateway.sendAndWait(AddBookingDetailCommand.builder()
                .bookingId(fromString(bookingId))
                .customerId(fromString(principal.getId()))
                .sportObjectPositionId(fromString(bookingDetail.getSportObjectPositionId()))
                .openingTimeId(fromString(bookingDetail.getOpeningTimeId()))
                .date(bookingDetail.getDate())
                .build());

        return noContent().build();
    }

    @DeleteMapping(CUSTOMER_API_BOOKING_DETAIL_BY_ID)
    ResponseEntity<?> deleteDetail(@PathVariable String bookingId,
                                   @PathVariable String bookingDetailId,
                                   @AuthenticationPrincipal UserPrincipal principal) {
        if (isInvalidUUID(bookingId) || isInvalidUUID(bookingDetailId)) {
            return badRequest().build();
        }

        commandGateway.sendAndWait(DeleteBookingDetailCommand.builder()
                .bookingId(fromString(bookingId))
                .bookingDetailId(fromString(bookingDetailId))
                .customerId(fromString(principal.getId()))
                .build());

        return noContent().build();
    }

    private ResponseEntity<?> changeState(String bookingId,
                                          UserPrincipal currentlySignedInUser,
                                          BiFunction<UUID, UUID, Object> getCommandFunction) {
        if (isInvalidUUID(bookingId)) {
            return badRequest().build();
        }

        commandGateway.sendAndWait(getCommandFunction.apply(
                fromString(bookingId),
                fromString(currentlySignedInUser.getId())));

        return noContent().build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AllSportObjectPositionsAlreadyBookedException.class)
    public List<FieldErrorDto> handleAlreadyBookedConflict() {
        return ImmutableList.of(new FieldErrorDto("sportObjectPositionId", ErrorCode.ALREADY_BOOKED));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SportObjectPositionNotExistsException.class)
    public List<FieldErrorDto> handleSportObjectPositionNotExistsConflict() {
        return ImmutableList.of(new FieldErrorDto("sportObjectPositionId", ErrorCode.NOT_EXISTS));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OpeningTimeNotExistsException.class)
    public List<FieldErrorDto> handleOpeningTimeNotExistsConflict() {
        return ImmutableList.of(new FieldErrorDto("openingTimeId", ErrorCode.NOT_EXISTS));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NotExistsException.class)
    public List<FieldErrorDto> handleDetailNotExistsConflict() {
        return ImmutableList.of(new FieldErrorDto("detailId", ErrorCode.NOT_EXISTS));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InvalidOpeningTimeAssignException.class)
    public List<FieldErrorDto> handleInvalidOpeningTimeConflict() {
        return ImmutableList.of(new FieldErrorDto("openingTimeId", ErrorCode.INVALID));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BookingDetailsLimitExceededException.class)
    public List<FieldErrorDto> handleDetailsLimitExceededConflict() {
        return ImmutableList.of(new FieldErrorDto("detailId", ErrorCode.LIMIT_EXCEEDED));
    }
}
