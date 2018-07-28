package domain.sportsclub;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;

import api.sportsclub.event.SportsclubCreatedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import query.model.sportsclub.SportsclubEntity;
import query.repository.SportsclubEntityRepository;

@Component
@AllArgsConstructor
public class SportsclubEventHandler {

    private static final Logger logger = getLogger(SportsclubEventHandler.class);

    private SportsclubEntityRepository sportsclubRepository;

    @EventHandler
    public void on(SportsclubCreatedEvent event) {
        logger.info("Saving sportsclub to database");
        SportsclubEntity sportsclub = new SportsclubEntity();
        copyProperties(event, sportsclub);
        sportsclub.setId(event.getSportsclubId());
        sportsclubRepository.save(sportsclub);
    }
}
