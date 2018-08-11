package domain.sportObject;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.UUID;

import api.sportObject.sportObjectPosition.event.SportObjectPositionCreatedEvent;
import api.sportObject.sportObjectPosition.event.SportObjectPositionDeletedEvent;
import api.sportObject.sportObjectPosition.event.SportObjectPositionUpdatedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import query.model.sportobject.SportObjectEntity;
import query.model.sportobject.SportObjectPositionEntity;
import query.repository.SportObjectEntityRepository;
import query.repository.SportObjectPositionEntityRepository;

@Component
@AllArgsConstructor
public class SportObjectPositionEntityEventHandler {

    private static final Logger logger = getLogger(SportObjectPositionEntityEventHandler.class);

    private SportObjectEntityRepository sportObjectRepository;
    private SportObjectPositionEntityRepository sportObjectPositionRepository;

    @EventHandler
    public void on(SportObjectPositionCreatedEvent event) {
        UUID objectId = event.getSportObjectId();
        UUID positionId = event.getSportObjectPositionId();
        logger.info("Saving sport object position with id: {} to sport object with id: {} to database", positionId, objectId);
        SportObjectEntity object = sportObjectRepository.getOne(objectId);
        SportObjectPositionEntity position = new SportObjectPositionEntity();
        copyProperties(event, position);
        position.setId(positionId);
        position.setSportObject(object);
        sportObjectPositionRepository.save(position);
    }

    @EventHandler
    public void on(SportObjectPositionUpdatedEvent event) {
        UUID positionId = event.getSportObjectPositionId();
        logger.info("Updating sport object position with id: {}", positionId);
        SportObjectPositionEntity position = sportObjectPositionRepository.getOne(positionId);
        copyProperties(event, position);
        sportObjectPositionRepository.save(position);
    }

    @EventHandler
    public void on(SportObjectPositionDeletedEvent event) {
        UUID positionId = event.getSportObjectPositionId();
        logger.info("Deleting sport object position with id: {} from database", positionId);
        SportObjectPositionEntity position = sportObjectPositionRepository.getOne(positionId);
        position.setDeleted(true);
        sportObjectPositionRepository.save(position);
    }
}
