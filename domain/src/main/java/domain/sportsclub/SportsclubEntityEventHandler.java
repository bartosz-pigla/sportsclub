package domain.sportsclub;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;
import static query.model.baseEntity.repository.BaseEntityQueryExpressions.idMatches;

import java.util.UUID;

import api.sportsclub.event.AnnouncementCreatedEvent;
import api.sportsclub.event.AnnouncementDeletedEvent;
import api.sportsclub.event.AnnouncementUpdatedEvent;
import api.sportsclub.event.SportsclubCreatedEvent;
import api.sportsclub.event.StatuteAddedEvent;
import api.sportsclub.event.StatuteUpdatedEvent;
import domain.common.exception.EntityNotExistsException;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import query.model.announcement.AnnouncementEntity;
import query.model.announcement.repository.AnnouncementEntityRepository;
import query.model.sportsclub.SportsclubEntity;
import query.model.sportsclub.StatuteEntity;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import query.model.sportsclub.repository.StatuteEntityRepository;

@Component
@AllArgsConstructor
public class SportsclubEntityEventHandler {

    private static final Logger logger = getLogger(SportsclubEntityEventHandler.class);

    private SportsclubEntityRepository sportsclubRepository;
    private StatuteEntityRepository statuteRepository;
    private AnnouncementEntityRepository announcementRepository;

    @EventHandler
    public void on(SportsclubCreatedEvent event) {
        logger.info("Saving sportsclub to database");
        SportsclubEntity sportsclub = new SportsclubEntity();
        copyProperties(event, sportsclub);
        sportsclub.setId(event.getSportsclubId());
        sportsclubRepository.save(sportsclub);
    }

    @EventHandler
    public void on(StatuteAddedEvent event) {
        logger.info("Saving statute to database");
        SportsclubEntity sportsclub = sportsclubRepository.getOne(event.getSportsclubId());
        StatuteEntity statute = new StatuteEntity();
        copyProperties(event, statute);
        statute.setSportsclub(sportsclub);
        statuteRepository.save(statute);
    }

    @EventHandler
    public void on(StatuteUpdatedEvent event) {
        logger.info("Updating statute to database");
        UUID statuteId = event.getStatuteId();
        StatuteEntity statute = statuteRepository.findOne(idMatches(statuteId)).orElseThrow(() -> new EntityNotExistsException(StatuteEntity.class, statuteId));
        copyProperties(event, statute);
        statuteRepository.save(statute);
    }

    @EventHandler
    public void on(AnnouncementCreatedEvent event) {
        logger.info("Saving announcement with title: {} to database", event.getTitle());
        SportsclubEntity sportsclub = sportsclubRepository.getOne(event.getSportsclubId());
        AnnouncementEntity announcement = new AnnouncementEntity();
        copyProperties(event, announcement);
        announcement.setLastModificationDate(event.getCreatedOn());
        announcement.setSportsclub(sportsclub);
        announcement.setId(event.getAnnouncementId());
        announcementRepository.save(announcement);
    }

    @EventHandler
    public void on(AnnouncementUpdatedEvent event) {
        UUID announcementId = event.getAnnouncementId();
        logger.info("Updating announcement with title: {} to database", event.getTitle());
        AnnouncementEntity announcement = announcementRepository.findOne(idMatches(announcementId)).orElseThrow(() -> new EntityNotExistsException(AnnouncementEntity.class, announcementId));
        copyProperties(event, announcement);
        announcement.setLastModificationDate(event.getCreatedOn());
        announcementRepository.save(announcement);
    }

    @EventHandler
    public void on(AnnouncementDeletedEvent event) {
        UUID announcementId = event.getAnnouncementId();
        logger.info("Deleting announcement with id: {} from database", announcementId);
        AnnouncementEntity announcement = announcementRepository.findOne(idMatches(announcementId)).orElseThrow(() -> new EntityNotExistsException(AnnouncementEntity.class, announcementId));
        announcement.setDeleted(true);
        announcementRepository.save(announcement);
    }
}
