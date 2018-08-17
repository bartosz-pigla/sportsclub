package domain.booking.service;

import static org.slf4j.LoggerFactory.getLogger;
import static query.model.booking.repository.BookingDetailQueryExpressions.bookingDetailMatches;

import java.util.Collection;
import java.util.UUID;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import api.booking.bookingDetail.command.DeleteBookingDetailCommand;
import domain.booking.Booking;
import domain.booking.exception.AllSportObjectPositionsAlreadyBookedException;
import domain.booking.exception.BookingDetailsLimitExceededException;
import domain.booking.exception.InvalidOpeningTimeAssignException;
import domain.common.exception.NotExistsException;
import domain.sportObject.exception.OpeningTimeNotExistsException;
import domain.sportObject.exception.SportObjectPositionNotExistsException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import query.model.booking.repository.BookingDetailEntityRepository;
import query.model.sportobject.OpeningTimeEntity;
import query.model.sportobject.SportObjectPositionEntity;
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import query.model.sportobject.repository.OpeningTimeQueryExpressions;
import query.model.sportobject.repository.SportObjectPositionEntityRepository;
import query.model.sportobject.repository.SportObjectPositionQueryExpressions;

@Service
@AllArgsConstructor
public final class BookingDetailValidator {

    public static final int DETAILS_LIMIT = 10;
    private static final Logger logger = getLogger(BookingDetailValidator.class);

    private BookingDetailEntityRepository bookingDetailRepository;
    private OpeningTimeEntityRepository openingTimeRepository;
    private SportObjectPositionEntityRepository sportObjectPositionRepository;

    public void validate(AddBookingDetailCommand command, Booking booking) {
        assertThatNotExceedsDetailsLimit(booking.getId(), booking.getDetails().size());

        UUID openingTimeId = command.getOpeningTimeId();
        OpeningTimeEntity openingTime = openingTimeRepository
                .findOne(OpeningTimeQueryExpressions.idMatches(openingTimeId))
                .orElseThrow(() -> {
                    logger.error("Booking detail with opening time id: {} not exists", openingTimeId);
                    return new OpeningTimeNotExistsException();
                });

        UUID sportObjectPositionId = command.getSportObjectPositionId();
        SportObjectPositionEntity sportObjectPosition = sportObjectPositionRepository
                .findOne(SportObjectPositionQueryExpressions.idMatches(sportObjectPositionId))
                .orElseThrow(() -> {
                    logger.error("Sport object position with id: {} not exists", sportObjectPositionId);
                    return new SportObjectPositionNotExistsException();
                });

        assertThatOpeningTimeIsAssignedToSportObjectPosition(openingTime, sportObjectPosition);

        int existingDetailsCount = (int) bookingDetailRepository
                .count(bookingDetailMatches(command.getSportObjectPositionId(), command.getDate(), openingTime.getTimeRange()));
        if (sportObjectPosition.getPositionsCount().getPositionsCount() <= existingDetailsCount) {
            logger.error("All available sport object positions with given date are already booked");
            throw new AllSportObjectPositionsAlreadyBookedException();
        }
    }

    public void validate(DeleteBookingDetailCommand command, Collection<UUID> detailIds) {
        UUID detailId = command.getBookingDetailId();

        if (!detailIds.contains(detailId)) {
            logger.error("Booking detail with id: {} not exists", detailId);
            throw new NotExistsException();
        }
    }

    private void assertThatOpeningTimeIsAssignedToSportObjectPosition(OpeningTimeEntity openingTimeEntity,
                                                                      SportObjectPositionEntity sportObjectPosition) {
        UUID sportObjectId = sportObjectPosition.getSportObject().getId();
        UUID openingTimeId = openingTimeEntity.getId();

        if (!openingTimeEntity.getSportObject().getId().equals(sportObjectId)) {
            logger.error("Opening time with id: {} is not assigned to sport object with id: {}", openingTimeId, sportObjectId);
            throw new InvalidOpeningTimeAssignException();
        }
    }

    private void assertThatNotExceedsDetailsLimit(UUID bookingId, int detailsCount) {
        if (detailsCount >= DETAILS_LIMIT) {
            logger.error("Booking with id: {} has max allowed positions count", bookingId);
            throw new BookingDetailsLimitExceededException();
        }
    }
}
