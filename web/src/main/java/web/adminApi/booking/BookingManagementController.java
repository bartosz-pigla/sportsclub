package web.adminApi.booking;

import static org.springframework.http.ResponseEntity.noContent;
import static web.common.RequestMappings.RECEPTIONIST_API_BOOKING_CONFIRM;
import static web.common.RequestMappings.RECEPTIONIST_API_BOOKING_FINISH;
import static web.common.RequestMappings.RECEPTIONIST_API_BOOKING_REJECT;

import java.util.UUID;
import java.util.function.Function;

import api.booking.command.ConfirmBookingCommand;
import api.booking.command.FinishBookingCommand;
import api.booking.command.RejectBookingCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import web.common.booking.BookingBaseController;

@RestController
final class BookingManagementController extends BookingBaseController {

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

    private ResponseEntity<?> changeState(UUID bookingId, Function<UUID, Object> sendCommandFunction) {
        commandGateway.sendAndWait(sendCommandFunction.apply(bookingId));
        return noContent().build();
    }
}
