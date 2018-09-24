package domain.sportObject;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.UUID;

import api.sportObject.event.SportObjectCreatedEvent;
import api.sportObject.event.SportObjectDeletedEvent;
import api.sportObject.event.SportObjectUpdatedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import query.model.sportobject.SportObjectEntity;
import query.model.sportobject.repository.SportObjectEntityRepository;
import query.model.sportsclub.SportsclubEntity;
import query.model.sportsclub.repository.SportsclubEntityRepository;

@Component
@AllArgsConstructor
public class SportObjectEntityEventHandler {

    private static final Logger logger = getLogger(SportObjectEntityEventHandler.class);

    private SportObjectEntityRepository sportObjectRepository;
    private SportsclubEntityRepository sportsclubRepository;

    @EventHandler
    public void on(SportObjectCreatedEvent event) {
        UUID sportObjectId = event.getSportObjectId();
        logger.info("Saving sport object with id: {} to database", sportObjectId);
        SportsclubEntity sportsclub = sportsclubRepository.getOne(event.getSportsclubId());
        SportObjectEntity sportObject = new SportObjectEntity();
        copyProperties(event, sportObject);
        sportObject.setId(sportObjectId);
        sportObject.setHeadquarter(sportsclub);
        sportObjectRepository.save(sportObject);
    }

    @EventHandler
    public void on(SportObjectUpdatedEvent event) {
        UUID sportObjectId = event.getSportObjectId();
        logger.info("Updating sport object with id: {} to database", sportObjectId);
        SportsclubEntity sportsclub = sportsclubRepository.getOne(event.getSportsclubId());
        SportObjectEntity sportObject = sportObjectRepository.getOne(sportObjectId);
        copyProperties(event, sportObject);
        sportObject.setHeadquarter(sportsclub);
        sportObjectRepository.save(sportObject);
    }

    @EventHandler
    public void on(SportObjectDeletedEvent event) {
        UUID sportObjectId = event.getSportObjectId();
        logger.info("Deleting sport object with id: {} from database", sportObjectId);
        SportObjectEntity sportObject = sportObjectRepository.getOne(sportObjectId);
        sportObject.setDeleted(true);
        sportObjectRepository.save(sportObject);
    }
}
