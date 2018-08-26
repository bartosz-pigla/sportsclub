package web.customerApi.booking.dto;

import query.model.booking.BookingDetailEntity;

public final class BookingDetailDtoFactory {

    public static BookingDetailDto create(BookingDetailEntity detail) {
        return BookingDetailDto.builder()
                .bookingDetailId(detail.getId().toString())
                .sportObjectPositionId(detail.getPosition().getId().toString())
                .openingTimeId(detail.getOpeningTime().getId().toString())
                .date(detail.getDate())
                .build();
    }
}
