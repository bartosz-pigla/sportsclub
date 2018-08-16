package query.model.booking.repository;

import java.time.LocalDate;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
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
            BooleanExpression bookingStateMatches = booking.bookingState.notIn(BookingState.SUBMITTED, BookingState.CONFIRMED, BookingState.FINISHED);

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

//    public static BooleanExpression openingTimeIdAndSportObjectPositionIdMatches(UUID openingTimeId, UUID sportObjectPositionId) {
//        if (openingTimeId != null && sportObjectPositionId != null) {
//            return bookingDetail.openingTime.id.eq(openingTimeId).and(bookingDetail.position.id.eq(sportObjectPositionId));
//        } else {
//            return bookingDetail.isNull();
//        }
//    }
//
//    public static BooleanExpression isBooked(BooleanExpression bookingDetailExpression) {
//        if (bookingDetailExpression != null) {
//            bookingDetailExpression.and(isNotDeleted(bookingDetail._super)).and()
//        } else {
//            return bookingDetail.isNull();
//        }
//    }
}
