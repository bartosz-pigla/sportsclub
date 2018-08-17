package boot;

import boot.populator.UserPopulator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan({
        "web",
        "api",
        "domain",
        "boot"
})
@EnableJpaRepositories(value = {
        "query",
        "org.axonframework.eventsourcing.eventstore.jpa",
        "org.axonframework.eventhandling.saga.repository.jpa"
})
@EntityScan(value = {
        "query",
        "org.axonframework.eventsourcing.eventstore.jpa",
        "org.axonframework.eventhandling.saga.repository.jpa"
})
public class SportsClubApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SportsClubApplication.class, args);
        context.getBean(UserPopulator.class).initializeDirector();
    }
}
