package domain.user;

import static org.springframework.beans.BeanUtils.copyProperties;

import api.user.event.ActivationLinkSentEvent;
import api.user.event.UserCreatedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import query.model.user.ActivationLinkEntry;
import query.model.user.UserEntity;
import query.repository.ActivationLinkEntryRepository;
import query.repository.UserEntityRepository;

@Component
@AllArgsConstructor
public class UserEventHandler {

    private UserEntityRepository userRepository;
    private ActivationLinkEntryRepository activationLinkRepository;

    @EventHandler
    public void on(UserCreatedEvent event) {
        UserEntity userEntity = new UserEntity();
        copyProperties(event, userEntity);
        userEntity.setId(event.getUserId());
        userEntity.setActivated(false);
        userRepository.save(userEntity);
    }

    @EventHandler
    public void on(ActivationLinkSentEvent event) {
        UserEntity user = userRepository.getOne(event.getCustomerId());
        ActivationLinkEntry activationLink = new ActivationLinkEntry(event.getDateTimeRange(), user);
        activationLinkRepository.save(activationLink);
    }
}
