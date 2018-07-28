package domain.sportsclub;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.io.Serializable;
import java.util.UUID;

import api.sportsclub.command.CreateSportsclubCommand;
import api.sportsclub.event.SportsclubCreatedEvent;
import domain.sportsclub.service.CreateSportsclubValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Sportsclub implements Serializable {

    private static final long serialVersionUID = -4854427363662768939L;

    @AggregateIdentifier
    private UUID sportsclubId;

    @CommandHandler
    public Sportsclub(CreateSportsclubCommand command, CreateSportsclubValidator validator) {
        validator.validate(command);
        SportsclubCreatedEvent event = new SportsclubCreatedEvent();
        copyProperties(command, event);
        event.setSportsclubId(UUID.randomUUID());
        apply(event);
    }

    @EventSourcingHandler
    public void on(SportsclubCreatedEvent event) {
        sportsclubId = event.getSportsclubId();
    }
}
