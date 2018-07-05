package boot;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventhandling.EventBus;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfiguration {

    private EventBus eventBus;

    private EntityManagerProvider entityManagerProvider;

    public AxonConfiguration(EventBus eventBus, EntityManagerProvider entityManagerProvider) {
        this.eventBus = eventBus;
        this.entityManagerProvider = entityManagerProvider;
    }

//    @Bean
//    public Repository<UserEntity> userRepository() {
//        return new GenericJpaRepository<UserEntity>(entityManagerProvider, UserEntity.class, eventBus);
//    }
}
