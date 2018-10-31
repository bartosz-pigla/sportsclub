package web.booking;

import static org.springframework.http.ResponseEntity.noContent;
import static web.common.RequestMappings.RECEPTIONIST_API_BOOKING;
import static web.common.RequestMappings.RECEPTIONIST_API_BOOKING_CONFIRM;
import static web.common.RequestMappings.RECEPTIONIST_API_BOOKING_FINISH;
import static web.common.RequestMappings.RECEPTIONIST_API_BOOKING_REJECT;

import java.util.UUID;
import java.util.function.Function;

import api.booking.command.ConfirmBookingCommand;
import api.booking.command.FinishBookingCommand;
import api.booking.command.RejectBookingCommand;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import query.model.booking.BookingEntity;
import web.booking.dto.BookingDto;
import web.booking.dto.BookingDtoFactory;
import web.user.dto.UserDtoFactory;

@RestController
final class BookingReceptionistController extends BookingBaseController {

    @PatchMapping(RECEPTIONIST_API_BOOKING_CONFIRM)
    ResponseEntity<?> confirm(@PathVariable UUID bookingId) {
        return changeState(bookingId, ConfirmBookingCommand::new);
    }

    @PatchMapping(RECEPTIONIST_API_BOOKING_REJECT)
    ResponseEntity<?> reject(@PathVariable UUID bookingId) {
        return changeState(bookingId, RejectBookingCommand::new);
    }

    @PatchMapping(RECEPTIONIST_API_BOOKING_FINISH)
    ResponseEntity<?> finish(@PathVariable UUID bookingId) {
        return changeState(bookingId, FinishBookingCommand::new);
    }

    @GetMapping(RECEPTIONIST_API_BOOKING)
    Page<BookingDto> get(@QuerydslPredicate(root = BookingEntity.class) Predicate predicate,
                         @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        return bookingRepository.findAll(predicate, pageable).map(b -> {
            BookingDto bookingDto = BookingDtoFactory.create(b);
            bookingDto.setCustomer(UserDtoFactory.create(b.getCustomer()));
            return bookingDto;
        });
    }

    private ResponseEntity<?> changeState(UUID bookingId, Function<UUID, Object> sendCommandFunction) {
        commandGateway.sendAndWait(sendCommandFunction.apply(bookingId));
        return noContent().build();
    }
}
