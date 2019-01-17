package web.booking;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.UUID.fromString;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static query.model.booking.repository.BookingQueryExpressions.idMatches;
import static query.model.booking.repository.BookingQueryExpressions.userIdMatches;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_CANCEL;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_DETAIL;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_DETAIL_BY_ID;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_SUBMIT;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import api.booking.bookingDetail.command.DeleteBookingDetailCommand;
import api.booking.command.CancelBookingCommand;
import api.booking.command.CreateBookingCommand;
import api.booking.command.SubmitBookingCommand;
import com.querydsl.core.types.Predicate;
import commons.ErrorCode;
import domain.booking.exception.AllSportObjectPositionsAlreadyBookedException;
import domain.booking.exception.BookingDetailsLimitExceededException;
import domain.booking.exception.InvalidOpeningTimeAssignException;
import domain.common.exception.NotExistsException;
import domain.sportObject.exception.OpeningTimeNotExistsException;
import domain.sportObject.exception.SportObjectPositionNotExistsException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.booking.BookingDetailEntity;
import query.model.booking.BookingEntity;
import query.model.booking.repository.BookingDetailEntityRepository;
import query.model.booking.repository.BookingDetailQueryExpressions;
import query.model.booking.repository.BookingEntityRepository;
import query.model.booking.repository.BookingQueryExpressions;
import web.booking.dto.BookingDetailDto;
import web.booking.dto.BookingDetailDtoFactory;
import web.booking.dto.BookingDto;
import web.booking.dto.BookingDtoFactory;
import web.common.dto.FieldErrorDto;
import web.signIn.dto.UserPrincipal;

@RestController
@Setter(onMethod_ = { @Autowired })
final class BookingCustomerController extends BookingBaseController {

    private BookingDetailEntityRepository bookingDetailRepository;
    private BookingEntityRepository bookingRepository;

    @PostMapping(CUSTOMER_API_BOOKING)
    ResponseEntity<?> create(@AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = fromString(principal.getId());
        commandGateway.sendAndWait(new CreateBookingCommand(userId));
        Page<BookingEntity> bookingPage = bookingRepository.findAll(
                userIdMatches(userId),
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "date")));

        if (bookingPage.getNumberOfElements() > 0) {
            return ok(BookingDtoFactory.create(bookingPage.getContent().get(0)));
        } else {
            return errorResponseService.create("userId", ErrorCode.NOT_EXISTS, HttpStatus.CONFLICT);
        }
    }

    @PatchMapping(CUSTOMER_API_BOOKING_CANCEL)
    ResponseEntity<?> cancel(@PathVariable UUID bookingId, @AuthenticationPrincipal UserPrincipal principal) {
        return changeState(bookingId, principal, CancelBookingCommand::new);
    }

    @PatchMapping(CUSTOMER_API_BOOKING_SUBMIT)
    ResponseEntity<?> submit(@PathVariable UUID bookingId, @AuthenticationPrincipal UserPrincipal principal) {
        return changeState(bookingId, principal, SubmitBookingCommand::new);
    }

    @PostMapping(CUSTOMER_API_BOOKING_DETAIL)
    ResponseEntity<?> addDetail(@PathVariable UUID bookingId,
                                @RequestBody BookingDetailDto bookingDetail,
                                @AuthenticationPrincipal UserPrincipal principal) {
        if (containsInvalidUUID(bookingDetail)) {
            return badRequest().build();
        }

        UUID sportObjectPositionId = fromString(bookingDetail.getSportObjectPositionId());
        UUID openingTimeId = fromString(bookingDetail.getOpeningTimeId());

        commandGateway.sendAndWait(AddBookingDetailCommand.builder()
                .bookingId(bookingId)
                .customerId(fromString(principal.getId()))
                .sportObjectPositionId(sportObjectPositionId)
                .openingTimeId(openingTimeId)
                .date(bookingDetail.getDate())
                .build());

        List<BookingDetailEntity> details = newArrayList(bookingDetailRepository.findAll(BookingDetailQueryExpressions.bookingIdMatches(bookingId)
                .and(BookingDetailQueryExpressions.sportObjectPositionIdMatches(sportObjectPositionId))
                .and(BookingDetailQueryExpressions.openingTimeIdMatches(openingTimeId))));

        return details.isEmpty() ? noContent().build() : ok(BookingDetailDtoFactory.create(details.get(0)));
    }

    @DeleteMapping(CUSTOMER_API_BOOKING_DETAIL_BY_ID)
    ResponseEntity<?> deleteDetail(@PathVariable UUID bookingId,
                                   @PathVariable UUID bookingDetailId,
                                   @AuthenticationPrincipal UserPrincipal principal) {
        commandGateway.sendAndWait(DeleteBookingDetailCommand.builder()
                .bookingId(bookingId)
                .bookingDetailId(bookingDetailId)
                .customerId(fromString(principal.getId()))
                .build());
        return noContent().build();
    }

    private ResponseEntity<?> changeState(UUID bookingId,
                                          UserPrincipal currentlySignedInUser,
                                          BiFunction<UUID, UUID, Object> getCommandFunction) {
        boolean bookingExists = bookingRepository.exists(idMatches(bookingId));

        if (bookingExists) {
            commandGateway.sendAndWait(getCommandFunction.apply(
                    bookingId,
                    fromString(currentlySignedInUser.getId())));
            return noContent().build();
        } else {
            return badRequest().build();
        }
    }

    @GetMapping(CUSTOMER_API_BOOKING)
    Page<BookingDto> get(@AuthenticationPrincipal UserPrincipal principal,
                         @QuerydslPredicate(root = BookingEntity.class) Predicate predicate,
                         @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        return this.bookingRepository
                .findAll(BookingQueryExpressions.userIdMatches(principal.getUser().getId()).and(predicate), pageable)
                .map(BookingDtoFactory::create);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AllSportObjectPositionsAlreadyBookedException.class)
    public List<FieldErrorDto> handleAlreadyBookedConflict() {
        return errorResponseService.createBody("sportObjectPositionId", ErrorCode.ALREADY_BOOKED);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SportObjectPositionNotExistsException.class)
    public List<FieldErrorDto> handleSportObjectPositionNotExistsConflict() {
        return errorResponseService.createBody("sportObjectPositionId", ErrorCode.NOT_EXISTS);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OpeningTimeNotExistsException.class)
    public List<FieldErrorDto> handleOpeningTimeNotExistsConflict() {
        return errorResponseService.createBody("openingTimeId", ErrorCode.NOT_EXISTS);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NotExistsException.class)
    public List<FieldErrorDto> handleDetailNotExistsConflict() {
        return errorResponseService.createBody("bookingDetailId", ErrorCode.NOT_EXISTS);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InvalidOpeningTimeAssignException.class)
    public List<FieldErrorDto> handleInvalidOpeningTimeConflict() {
        return errorResponseService.createBody("openingTimeId", ErrorCode.INVALID);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BookingDetailsLimitExceededException.class)
    public List<FieldErrorDto> handleDetailsLimitExceededConflict() {
        return errorResponseService.createBody("bookingDetailId", ErrorCode.LIMIT_EXCEEDED);
    }
}
