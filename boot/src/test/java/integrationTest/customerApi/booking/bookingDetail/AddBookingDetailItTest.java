package integrationTest.customerApi.booking.bookingDetail;

import static org.junit.Assert.assertEquals;
import static query.model.booking.repository.BookingDetailQueryExpressions.bookingDetailMatches;
import static query.model.booking.repository.BookingQueryExpressions.userIdMatches;
import static query.model.sportobject.repository.OpeningTimeQueryExpressions.idMatches;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_DETAIL;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import api.booking.bookingDetail.command.AddBookingDetailCommand;
import api.booking.command.CreateBookingCommand;
import commons.ErrorCode;
import integrationTest.AbstractBookingItTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import query.model.booking.BookingState;
import query.model.embeddable.OpeningTimeRange;
import web.booking.dto.BookingDetailDto;

public final class AddBookingDetailItTest extends AbstractBookingItTest {

    @Test
    @DirtiesContext
    public void shouldAdd() {
        signIn("customer", "password");
        commandGateway.sendAndWait(new CreateBookingCommand(customerId));
        UUID bookingId = bookingRepository.findOne(userIdMatches(customerId)).get().getId();
        LocalDate date = LocalDate.now();

        BookingDetailDto bookingDetail = BookingDetailDto.builder()
                .openingTimeId(openingTimeId.toString())
                .sportObjectPositionId(sportObjectPositionId.toString())
                .date(date)
                .build();

        ResponseEntity<String> addDetailResponse = restTemplate.postForEntity(
                CUSTOMER_API_BOOKING_DETAIL,
                bookingDetail,
                String.class,
                bookingId);

        assertEquals(addDetailResponse.getStatusCode(), HttpStatus.OK);
        assertEquals(bookingRepository.findOne(userIdMatches(customerId)).get().getState(), BookingState.CREATED);
        OpeningTimeRange timeRange = openingTimeRepository.findOne(idMatches(openingTimeId)).get().getTimeRange();

        assertEquals(bookingDetailRepository.findOne(
                bookingDetailMatches(sportObjectPositionId, date, timeRange)).get().getOpeningTime().getTimeRange(),
                timeRange);
    }

    @Test
    @DirtiesContext
    public void shouldNotAddWhenAllSportObjectPositionsAreAlreadyBooked() {
        signIn("customer", "password");
        commandGateway.sendAndWait(new CreateBookingCommand(customerId));
        UUID bookingId = bookingRepository.findOne(userIdMatches(customerId)).get().getId();
        LocalDate date = LocalDate.now();

        commandGateway.sendAndWait(AddBookingDetailCommand.builder()
                .bookingId(bookingId)
                .customerId(customerId)
                .openingTimeId(openingTimeId)
                .sportObjectPositionId(sportObjectPositionId)
                .date(date)
                .build());

        BookingDetailDto bookingDetail = BookingDetailDto.builder()
                .openingTimeId(openingTimeId.toString())
                .sportObjectPositionId(sportObjectPositionId.toString())
                .date(LocalDate.now())
                .build();

        ResponseEntity<List> addDetailResponse = restTemplate.postForEntity(
                CUSTOMER_API_BOOKING_DETAIL,
                bookingDetail,
                List.class,
                bookingId);

        assertEquals(addDetailResponse.getStatusCode(), HttpStatus.CONFLICT);
        assertField("sportObjectPositionId", ErrorCode.ALREADY_BOOKED.getCode(), addDetailResponse.getBody());
        assertEquals(bookingRepository.findOne(userIdMatches(customerId)).get().getState(), BookingState.CREATED);
        OpeningTimeRange timeRange = openingTimeRepository.findOne(idMatches(openingTimeId)).get().getTimeRange();

        assertEquals(bookingDetailRepository.findOne(
                bookingDetailMatches(sportObjectPositionId, LocalDate.now(), timeRange)).get().getOpeningTime().getTimeRange(),
                timeRange);
    }
}
