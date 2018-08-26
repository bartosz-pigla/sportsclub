package web.customerApi.booking.dto;

import query.model.booking.BookingEntity;

public final class BookingDtoFactory {

    public static BookingDto create(BookingEntity booking) {
        return BookingDto.builder()
                .id(booking.getId().toString())
                .customerId(booking.getCustomer().getId().toString())
                .date(booking.getDate())
                .state(booking.getState())
                .build();
    }
}
