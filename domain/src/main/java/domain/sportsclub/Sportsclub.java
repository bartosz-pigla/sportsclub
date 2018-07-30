package domain.sportsclub;

import static com.google.common.collect.Sets.newHashSet;
import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import api.sportsclub.command.CreateAnnouncementCommand;
import api.sportsclub.command.CreateSportsclubCommand;
import api.sportsclub.command.DeleteAnnouncementCommand;
import api.sportsclub.command.UpdateAnnouncementCommand;
import api.sportsclub.command.UpdateStatuteCommand;
import api.sportsclub.event.AnnouncementCreatedEvent;
import api.sportsclub.event.AnnouncementDeletedEvent;
import api.sportsclub.event.AnnouncementUpdatedEvent;
import api.sportsclub.event.SportsclubCreatedEvent;
import api.sportsclub.event.StatuteAddedEvent;
import api.sportsclub.event.StatuteUpdatedEvent;
import domain.sportsclub.service.AnnouncementValidator;
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
    private Set<UUID> announcementIds = newHashSet();
    private UUID statuteId;

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

    @CommandHandler
    public void on(UpdateStatuteCommand command) {
        if (statuteId == null) {
            addStatute(command);
        } else {
            updateStatute(command);
        }
    }

    private void addStatute(UpdateStatuteCommand command) {
        StatuteAddedEvent event = new StatuteAddedEvent();
        copyProperties(command, event);
        event.setStatuteId(UUID.randomUUID());
        apply(event);
    }

    private void updateStatute(UpdateStatuteCommand command) {
        StatuteUpdatedEvent event = new StatuteUpdatedEvent();
        copyProperties(command, event);
        event.setStatuteId(statuteId);
        apply(event);
    }

    @EventSourcingHandler
    public void on(StatuteAddedEvent event) {
        statuteId = event.getStatuteId();
    }

    @CommandHandler
    public void on(CreateAnnouncementCommand command) {
        AnnouncementCreatedEvent event = new AnnouncementCreatedEvent();
        copyProperties(command, event);
        event.setAnnouncementId(UUID.randomUUID());
        apply(event);
    }

    @EventSourcingHandler
    public void on(AnnouncementCreatedEvent event) {
        announcementIds.add(event.getAnnouncementId());
    }

    @CommandHandler
    public void on(UpdateAnnouncementCommand command, AnnouncementValidator validator) {
        validator.validate(command.getAnnouncementId(), this);
        AnnouncementUpdatedEvent event = new AnnouncementUpdatedEvent();
        copyProperties(command, event);
        event.setAnnouncementId(event.getAnnouncementId());
        apply(event);
    }

    @CommandHandler
    public void on(DeleteAnnouncementCommand command, AnnouncementValidator validator) {
        UUID announcementId = command.getAnnouncementId();
        validator.validate(announcementId, this);
        apply(new AnnouncementDeletedEvent(announcementId));
    }

    @EventSourcingHandler
    public void on(AnnouncementDeletedEvent event) {
        announcementIds.remove(event.getAnnouncementId());
    }
}
