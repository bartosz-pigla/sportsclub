package domain.sportObject;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import api.sportObject.command.CreateOpeningTimeCommand;
import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.command.DeleteOpeningTimeCommand;
import api.sportObject.command.DeleteSportObjectCommand;
import api.sportObject.command.UpdateOpeningTimeCommand;
import api.sportObject.command.UpdateSportObjectCommand;
import api.sportObject.event.OpeningTimeCreatedEvent;
import api.sportObject.event.OpeningTimeDeletedEvent;
import api.sportObject.event.OpeningTimeUpdatedEvent;
import api.sportObject.event.SportObjectCreatedEvent;
import api.sportObject.event.SportObjectDeletedEvent;
import api.sportObject.event.SportObjectUpdatedEvent;
import domain.sportObject.service.OpeningTimeValidator;
import domain.sportObject.service.SportObjectValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateMember;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SportObject implements Serializable {

    private static final long serialVersionUID = 6573077212523214115L;

    @AggregateIdentifier
    private UUID sportObjectId;
    private String name;
    @AggregateMember
    private Set<OpeningTime> openingHours;
    private boolean deleted;

    @CommandHandler
    public SportObject(CreateSportObjectCommand command, SportObjectValidator validator) {
        validator.validate(command);
        SportObjectCreatedEvent event = new SportObjectCreatedEvent();
        copyProperties(command, event);
        event.setSportObjectId(UUID.randomUUID());
        apply(event);
    }

    @EventSourcingHandler
    public void on(SportObjectCreatedEvent event) {
        sportObjectId = event.getSportObjectId();
        name = event.getName();
        openingHours = new HashSet<>();
    }

    @CommandHandler
    public void on(UpdateSportObjectCommand command, SportObjectValidator validator) {
        validator.validate(command, this);
        SportObjectUpdatedEvent event = new SportObjectUpdatedEvent();
        copyProperties(command, event);
        apply(event);
    }

    @EventSourcingHandler
    public void on(SportObjectUpdatedEvent event) {
        name = event.getName();
    }

    @CommandHandler
    public void on(DeleteSportObjectCommand command, SportObjectValidator validator) {
        validator.assertThatIsNotDeleted(deleted, name);
        SportObjectDeletedEvent event = new SportObjectDeletedEvent();
        copyProperties(command, event);
        apply(event);
    }

    @EventSourcingHandler
    public void on(SportObjectDeletedEvent event) {
        deleted = true;
    }

    @CommandHandler
    public void on(CreateOpeningTimeCommand command, OpeningTimeValidator validator) {
        validator.validate(command, this);
        OpeningTimeCreatedEvent event = new OpeningTimeCreatedEvent();
        copyProperties(command, event);
        event.setOpeningTimeId(UUID.randomUUID());
        apply(event);
    }

    @EventSourcingHandler
    public void on(OpeningTimeCreatedEvent event) {
        openingHours.add(OpeningTime.builder()
                .openingTimeId(event.getOpeningTimeId())
                .openingTimeRange(event.getDateRange())
                .price(event.getPrice()).build());
    }

    @CommandHandler
    public void on(UpdateOpeningTimeCommand command, OpeningTimeValidator validator) {
        validator.validate(command, this);
        OpeningTimeUpdatedEvent event = new OpeningTimeUpdatedEvent();
        copyProperties(command, event);
        apply(event);
    }

    @CommandHandler
    public void on(DeleteOpeningTimeCommand command, OpeningTimeValidator validator) {
        validator.validate(command, this);
        OpeningTimeDeletedEvent event = new OpeningTimeDeletedEvent();
        copyProperties(command, event);
        apply(event);
    }
}
