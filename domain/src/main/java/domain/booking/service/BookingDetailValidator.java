package domain.booking.service;

import static org.slf4j.LoggerFactory.getLogger;
import static query.model.booking.repository.BookingDetailQueryExpressions.bookingDetailMatches;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import domain.booking.Booking;
import domain.booking.exception.BookingDetailsLimitExceededException;
import domain.booking.exception.BookingWithGivenDateAndPositionAlreadyExists;
import domain.booking.exception.OpeningTimeNotExistsException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import query.model.booking.repository.BookingDetailEntityRepository;
import query.model.embeddable.OpeningTimeRange;
import query.model.sportobject.OpeningTimeEntity;
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import query.model.sportobject.repository.OpeningTimeQueryExpressions;

@Service
@AllArgsConstructor
public final class BookingDetailValidator {

    private static final int DETAILS_LIMIT = 10;
    private static final Logger logger = getLogger(BookingDetailValidator.class);

    private BookingDetailEntityRepository bookingDetailRepository;
    private OpeningTimeEntityRepository openingTimeRepository;

    public void validate(AddBookingDetailCommand command, Booking booking) {
        assertThatNotExceedsDetailsLimit(booking.getBookingId(), booking.getBookingDetails());

        UUID openingTimeId = command.getOpeningTimeId();
        Optional<OpeningTimeEntity> openingTimeOptional = openingTimeRepository
                .findOne(OpeningTimeQueryExpressions.idMatches(openingTimeId));

        if (openingTimeOptional.isPresent()) {
            assertThatHasUniqueBookingDateInOtherBookings(command, openingTimeOptional.get().getTimeRange());
        } else {
            throwOpeningTimeNotExistsException(openingTimeId);
        }
    }

    private void assertThatNotExceedsDetailsLimit(UUID bookingId, Collection<UUID> bookingDetails) {
        if (bookingDetails.size() >= DETAILS_LIMIT) {
            logger.error("Booking with id: {} has max allowed positions count", bookingId);
            throw new BookingDetailsLimitExceededException();
        }
    }

    private void assertThatHasUniqueBookingDateInOtherBookings(AddBookingDetailCommand command, OpeningTimeRange timeRange) {
        bookingDetailRepository.findOne(bookingDetailMatches(command.getSportObjectPositionId(), command.getDate(), timeRange))
                .ifPresent(bookingDetail -> throwBookingWithGivenDateAndPositionAlreadyExistsException(bookingDetail.getBooking().getId()));
    }

    private void throwOpeningTimeNotExistsException(UUID openingTimeId) {
        logger.error("Booking detail with opening time id: {} not exists", openingTimeId);
        throw new OpeningTimeNotExistsException();
    }

    private void throwBookingWithGivenDateAndPositionAlreadyExistsException(UUID bookingId) {
        logger.error("Booking with id: {} already has detail with the same booking date", bookingId);
        throw new BookingWithGivenDateAndPositionAlreadyExists();
    }
}
