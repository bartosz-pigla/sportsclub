package domain.user;

import static org.springframework.beans.BeanUtils.copyProperties;

import api.user.event.UserCreatedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import query.model.user.UserEntity;
import query.repository.UserEntityRepository;

@Component
@AllArgsConstructor
public class UserEventHandler {

    private UserEntityRepository userEntityRepository;

    @EventHandler
    public void on(UserCreatedEvent event) {
        UserEntity userEntity = new UserEntity();
        copyProperties(event, userEntity);
        userEntity.setActivated(false);
        userEntityRepository.save(userEntity);
    }
}
