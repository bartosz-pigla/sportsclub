package query.model.booking.repository;

import java.time.LocalDate;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.repository.BaseEntityQueryExpressions;
import query.model.booking.BookingState;
import query.model.booking.QBookingDetailEntity;
import query.model.booking.QBookingEntity;
import query.model.embeddable.OpeningTimeRange;

public final class BookingDetailQueryExpressions {

    private static final QBookingDetailEntity bookingDetail = QBookingDetailEntity.bookingDetailEntity;
    private static final QBookingEntity booking = bookingDetail.booking;

    public static BooleanExpression bookingDetailMatches(UUID sportObjectPositionId, LocalDate date, OpeningTimeRange timeRange) {
        if (sportObjectPositionId != null && date != null && timeRange != null) {
            BooleanExpression bookingIsNotDeleted = booking.deleted.eq(false);
            BooleanExpression bookingStateMatches = booking.state.notIn(BookingState.SUBMITTED, BookingState.CONFIRMED, BookingState.FINISHED);

            BooleanExpression bookingDetailSportObjectPositionMatches = bookingDetail.position.id.eq(sportObjectPositionId);
            BooleanExpression bookingDetailDateMatches = bookingDetail.date.eq(date);
            BooleanExpression bookingDetailTimeRangeMatches = bookingDetail.openingTime.timeRange.eq(timeRange);

            BooleanExpression bookingMatches = bookingIsNotDeleted.and(bookingStateMatches);
            BooleanExpression bookingDetailMatches = bookingDetailDateMatches.and(bookingDetailTimeRangeMatches).and(bookingDetailSportObjectPositionMatches);

            return bookingMatches.and(bookingDetailMatches);
        } else {
            return bookingDetail.isNull();
        }
    }

    public static BooleanExpression idMatches(UUID id) {
        return BaseEntityQueryExpressions.idMatches(id, bookingDetail._super);
    }
}
