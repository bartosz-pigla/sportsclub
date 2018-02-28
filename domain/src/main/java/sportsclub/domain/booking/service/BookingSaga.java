package sportsclub.domain.booking.service;

import lombok.Setter;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.saga.EndSaga;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import sportsclub.api.booking.event.BookingChangedEvent;
import sportsclub.api.booking.event.BookingConfirmedEvent;
import sportsclub.api.booking.event.BookingStartedEvent;
import sportsclub.query.booking.service.BookingEntryRepository;

import java.time.Instant;
import java.util.Set;

@Saga
public class BookingSaga {
    @Setter
    private transient CommandBus commandBus;
    @Setter
    private transient BookingEntryRepository bookingEntryRepository;

    private String bookingId;
    private String userId;
    private String roomId;
    private Set<String> places;
    private Instant bookingCreationTime;
    private Instant bookingTime;

    @StartSaga
    @SagaEventHandler(associationProperty = "bookingId")
    public void handle(BookingStartedEvent event) {
        bookingId = event.getBookingId();
        userId = event.getUserId();
        roomId = event.getRoomId();
        places = event.getPlaces();
        bookingCreationTime = event.getBookingCreationTime();
        bookingTime = event.getBookingTime();
    }

    @SagaEventHandler(associationProperty = "bookingId")
    public void handle(BookingChangedEvent event) {
        places = event.getPlaces();
    }

    @SagaEventHandler(associationProperty = "bookingId")
    @EndSaga
    public void handle(BookingConfirmedEvent event){

    }
}
