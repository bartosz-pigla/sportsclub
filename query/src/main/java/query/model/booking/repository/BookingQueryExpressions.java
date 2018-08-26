package query.model.booking.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import java.util.Optional;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.baseEntity.repository.BaseEntityQueryExpressions;
import query.model.booking.QBookingEntity;
import query.model.user.QUserEntity;
import query.model.user.UserEntity;

public final class BookingQueryExpressions {

    private static final QBookingEntity booking = QBookingEntity.bookingEntity;
    private static final QUserEntity user = QUserEntity.userEntity;

    public static BooleanExpression idMatches(UUID id) {
        return BaseEntityQueryExpressions.idMatches(id, booking._super);
    }

    public static BooleanExpression usernameMatches(String username) {
        return Optional.ofNullable(username)
                .map(u -> isNotDeleted(booking._super).and(booking.customer.username.eq(username)))
                .orElse(booking.isNull());
    }

    public static BooleanExpression userMatches(UserEntity user) {
        return Optional.ofNullable(user)
                .map(u -> userIdMatches(u.getId()))
                .orElse(booking.isNull());
    }

    public static BooleanExpression userIdMatches(UUID id) {
        return Optional.ofNullable(id)
                .map(i -> isNotDeleted(booking._super).and(booking.customer.id.eq(i)))
                .orElse(booking.isNull());
    }

    public static BooleanExpression bookingIdAndUserIdMatches(UUID bookingId, UUID userId) {
        return Optional.ofNullable(userId)
                .map(i -> idMatches(bookingId).and(isNotDeleted(booking.customer._super)).and(booking.customer.id.eq(userId)))
                .orElse(booking.isNull());
//        if (bookingId != null && userId != null) {
//            return isNotDeleted(booking._super).and(isNotDeleted(booking.customer._super)).and(booking.customer.id.eq(userId));
//        } else {
//            return booking.isNull();
//        }
    }
}
