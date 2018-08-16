package domain.booking;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.UUID;

import api.booking.bookingDetail.event.BookingDetailAddedEvent;
import api.booking.bookingDetail.event.BookingDetailDeletedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import query.model.booking.BookingDetailEntity;
import query.model.booking.BookingEntity;
import query.model.booking.repository.BookingDetailEntityRepository;
import query.model.booking.repository.BookingEntityRepository;
import query.model.sportobject.OpeningTimeEntity;
import query.model.sportobject.SportObjectPositionEntity;
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import query.model.sportobject.repository.SportObjectPositionEntityRepository;

@Component
@AllArgsConstructor
public class BookingDetailEventHandler {

    private static final Logger logger = getLogger(BookingDetailEventHandler.class);

    private BookingEntityRepository bookingRepository;
    private BookingDetailEntityRepository bookingDetailRepository;
    private OpeningTimeEntityRepository openingTimeRepository;
    private SportObjectPositionEntityRepository sportObjectPositionRepository;

    @EventHandler
    public void on(BookingDetailAddedEvent event) {
        UUID bookingId = event.getBookingId();
        UUID detailId = event.getBookingDetailId();
        logger.info("Adding detail with id: {} to booking with id: {}", detailId, bookingId);

        BookingEntity booking = bookingRepository.getOne(bookingId);
        OpeningTimeEntity openingTime = openingTimeRepository.getOne(event.getOpeningTimeId());
        SportObjectPositionEntity sportObjectPosition = sportObjectPositionRepository.getOne(event.getSportObjectPositionId());

        BookingDetailEntity detail = new BookingDetailEntity();
        copyProperties(event, detail);
        detail.setId(detailId);
        detail.setBooking(booking);
        detail.setOpeningTime(openingTime);
        detail.setPosition(sportObjectPosition);
        bookingDetailRepository.save(detail);
    }

    @EventHandler
    public void on(BookingDetailDeletedEvent event) {
        UUID detailId = event.getBookingDetailId();
        logger.info("Deleting detail with id: {}", detailId);

        BookingDetailEntity detail = bookingDetailRepository.getOne(detailId);
        detail.setDeleted(true);
        bookingDetailRepository.save(detail);
    }
}
