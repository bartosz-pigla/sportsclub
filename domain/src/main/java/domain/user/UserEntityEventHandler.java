package domain.user;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;
import static query.model.user.repository.UserQueryExpressions.idMatches;

import java.util.UUID;

import api.user.event.ActivationLinkSentEvent;
import api.user.event.UserActivatedEvent;
import api.user.event.UserCreatedEvent;
import api.user.event.UserDeactivatedEvent;
import api.user.event.UserDeletedEvent;
import domain.common.exception.EntityNotExistsException;
import domain.user.createUser.service.ActivationLinkService;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import query.model.user.ActivationLinkEntry;
import query.model.user.UserEntity;
import query.model.user.repository.ActivationLinkEntryRepository;
import query.model.user.repository.UserEntityRepository;

@Component
@AllArgsConstructor
public class UserEntityEventHandler {

    private static final Logger logger = getLogger(UserEntityEventHandler.class);

    private UserEntityRepository userRepository;
    private ActivationLinkEntryRepository activationLinkRepository;
    private ActivationLinkService activationLinkService;

    @EventHandler
    public void on(UserCreatedEvent event) {
        logger.info("Saving user to database");
        UserEntity userEntity = new UserEntity();
        copyProperties(event, userEntity);
        userEntity.setId(event.getUserId());
        userEntity.setActivated(false);
        userRepository.save(userEntity);
    }

    @EventHandler
    public void on(ActivationLinkSentEvent event) {
        logger.info("Sending activation link");
        UserEntity user = userRepository.getOne(event.getCustomerId());
        ActivationLinkEntry activationLink = new ActivationLinkEntry(event.getActivationKey(), event.getDateTimeRange(), user);
        activationLinkRepository.save(activationLink);
        activationLinkService.send(user.getEmail(), activationLink.getId());
    }

    @EventHandler
    public void on(UserActivatedEvent event) {
        logger.info("Activating user");
        changeUserActivation(event.getUserId(), true);
    }

    @EventHandler
    public void on(UserDeactivatedEvent event) {
        logger.info("Deactivation user");
        changeUserActivation(event.getUserId(), false);
    }

    private void changeUserActivation(UUID userId, boolean isActivated) {
        UserEntity user = userRepository.findOne(idMatches(userId)).orElseThrow(() -> new EntityNotExistsException(UserEntity.class, userId));
        user.setActivated(isActivated);
        userRepository.save(user);
    }

    @EventHandler
    public void on(UserDeletedEvent event) {
        logger.info("Deleting customer");
        UUID userId = event.getUserId();
        UserEntity user = userRepository.findOne(idMatches(userId)).orElseThrow(() -> new EntityNotExistsException(UserEntity.class, userId));
        user.setDeleted(true);
        userRepository.save(user);
    }
}
