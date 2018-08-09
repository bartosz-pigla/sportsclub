package domain.sportObject;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.UUID;

import api.sportObject.sportObjectPosition.event.SportObjectPositionCreatedEvent;
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
        UUID sportObjectId = event.getSportObjectId();
        UUID sportObjectPositionId = event.getSportObjectPositionId();
        logger.info("Saving sport object position with id: {} to sport object with id: {} to database", sportObjectPositionId, sportObjectId);
        SportObjectEntity sportObject = sportObjectRepository.getOne(sportObjectId);
        SportObjectPositionEntity sportObjectPosition = new SportObjectPositionEntity();
        copyProperties(event, sportObjectPosition);
        sportObjectPosition.setId(sportObjectPositionId);
        sportObjectPosition.setSportObject(sportObject);
        sportObjectPositionRepository.save(sportObjectPosition);
    }
}
