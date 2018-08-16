package domain.sportObject;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.UUID;

import api.sportObject.openingTime.event.OpeningTimeCreatedEvent;
import api.sportObject.openingTime.event.OpeningTimeDeletedEvent;
import api.sportObject.openingTime.event.OpeningTimeUpdatedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import query.model.sportobject.OpeningTimeEntity;
import query.model.sportobject.SportObjectEntity;
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import query.model.sportobject.repository.SportObjectEntityRepository;

@Component
@AllArgsConstructor
public class OpeningTimeEntityEventHandler {

    private static final Logger logger = getLogger(OpeningTimeEntityEventHandler.class);

    private SportObjectEntityRepository sportObjectRepository;
    private OpeningTimeEntityRepository openingTimeRepository;

    @EventHandler
    public void on(OpeningTimeCreatedEvent event) {
        UUID openingTimeId = event.getOpeningTimeId();
        logger.info("Saving opening time object with id: {} to database", openingTimeId);
        SportObjectEntity sportObject = sportObjectRepository.getOne(event.getSportObjectId());
        OpeningTimeEntity openingTime = new OpeningTimeEntity();
        copyProperties(event, openingTime);
        openingTime.setId(openingTimeId);
        openingTime.setSportObject(sportObject);
        openingTimeRepository.save(openingTime);
    }

    @EventHandler
    public void on(OpeningTimeUpdatedEvent event) {
        UUID openingTimeId = event.getOpeningTimeId();
        logger.info("Updating opening time object with id: {} to database", openingTimeId);
        OpeningTimeEntity openingTime = openingTimeRepository.getOne(openingTimeId);
        openingTime.setTimeRange(event.getTimeRange());
        openingTime.setPrice(event.getPrice());
        openingTimeRepository.save(openingTime);
    }

    @EventHandler
    public void on(OpeningTimeDeletedEvent event) {
        UUID openingTimeId = event.getOpeningTimeId();
        logger.info("Deleting object with id: {} from database", openingTimeId);
        OpeningTimeEntity openingTime = openingTimeRepository.getOne(openingTimeId);
        openingTime.setDeleted(true);
        openingTimeRepository.save(openingTime);
    }
}
