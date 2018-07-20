package domain.user;

import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.UUID;

import api.user.event.ActivationLinkSentEvent;
import api.user.event.CustomerActivatedEvent;
import api.user.event.UserCreatedEvent;
import domain.common.exception.EntityNotExistsException;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import query.model.user.ActivationLinkEntry;
import query.model.user.UserEntity;
import query.repository.ActivationLinkEntryRepository;
import query.repository.UserEntityRepository;

@Component
@AllArgsConstructor
public class UserEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserEventHandler.class);

    private UserEntityRepository userRepository;
    private ActivationLinkEntryRepository activationLinkRepository;

    @EventHandler
    public void on(UserCreatedEvent event) {
        logger.info("saving user to database");
        UserEntity userEntity = new UserEntity();
        copyProperties(event, userEntity);
        userEntity.setId(event.getUserId());
        userEntity.setActivated(false);
        userRepository.save(userEntity);
    }

    @EventHandler
    public void on(ActivationLinkSentEvent event) {
        logger.info("sending activation link");
        UserEntity user = userRepository.getOne(event.getCustomerId());
        ActivationLinkEntry activationLink = new ActivationLinkEntry(event.getActivationKey(), event.getDateTimeRange(), user);
        activationLinkRepository.save(activationLink);
        System.out.println(activationLink.getId());
    }

    @EventHandler
    public void on(CustomerActivatedEvent event) {
        logger.info("activating user");
        UUID customerId = event.getCustomerId();
        UserEntity user = userRepository.findById(customerId).orElseThrow(() -> new EntityNotExistsException(UserEntity.class, customerId));
        user.setActivated(true);
        userRepository.save(user);
    }
}
