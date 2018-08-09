package domain.sportObject;

import static com.google.common.collect.Sets.newHashSet;
import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.command.DeleteSportObjectCommand;
import api.sportObject.command.UpdateSportObjectCommand;
import api.sportObject.event.SportObjectCreatedEvent;
import api.sportObject.event.SportObjectDeletedEvent;
import api.sportObject.event.SportObjectUpdatedEvent;
import api.sportObject.openingTime.command.CreateOpeningTimeCommand;
import api.sportObject.openingTime.command.DeleteOpeningTimeCommand;
import api.sportObject.openingTime.command.UpdateOpeningTimeCommand;
import api.sportObject.openingTime.event.OpeningTimeCreatedEvent;
import api.sportObject.openingTime.event.OpeningTimeDeletedEvent;
import api.sportObject.openingTime.event.OpeningTimeUpdatedEvent;
import api.sportObject.sportObjectPosition.command.CreateSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.command.DeleteSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.command.UpdateSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.event.SportObjectPositionCreatedEvent;
import api.sportObject.sportObjectPosition.event.SportObjectPositionDeletedEvent;
import api.sportObject.sportObjectPosition.event.SportObjectPositionUpdatedEvent;
import domain.sportObject.service.OpeningTimeValidator;
import domain.sportObject.service.SportObjectPositionValidator;
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
    @AggregateMember
    private Set<SportObjectPosition> positions;
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
        positions = new HashSet<>();
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

    @CommandHandler
    public void on(CreateSportObjectPositionCommand command, SportObjectPositionValidator validator) {
        validator.validate(command, this);
        SportObjectPositionCreatedEvent event = new SportObjectPositionCreatedEvent();
        copyProperties(command, event);
        event.setSportObjectId(UUID.randomUUID());
        apply(event);
    }

    @EventSourcingHandler
    public void on(SportObjectPositionCreatedEvent event) {
        positions.add(new SportObjectPosition(event.getSportObjectPositionId(), event.getName()));
    }

    @CommandHandler
    public void on(DeleteSportObjectPositionCommand command, SportObjectPositionValidator validator) {
        validator.validate(command, this);
        SportObjectPositionDeletedEvent event = new SportObjectPositionDeletedEvent(command.getSportObjectPositionId());
        apply(event);
    }

    @CommandHandler
    public void on(UpdateSportObjectPositionCommand command, SportObjectPositionValidator validator) {
        validator.validate(command, this);
        SportObjectPositionUpdatedEvent event = new SportObjectPositionUpdatedEvent();
        copyProperties(command, event);
        apply(event);
    }

    public Optional<SportObjectPosition> findPositionById(UUID positionId) {
        return positions.stream().filter(p -> p.getSportObjectPositionId().equals(positionId)).findFirst();
    }

    public Optional<SportObjectPosition> findPositionByNameExcept(String name, SportObjectPosition excludedPosition) {
        Set<SportObjectPosition> positions = newHashSet(this.positions);
        positions.remove(excludedPosition);
        return positions.stream().filter(p -> p.getName().equals(name)).findFirst();
    }

    public Optional<SportObjectPosition> findPositionByName(String name) {
        return positions.stream().filter(p -> p.getName().equals(name)).findFirst();
    }
}
