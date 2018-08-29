package boot;

import boot.populator.SportsclubPopulator;
import boot.populator.UserPopulator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
        context.getBean(UserPopulator.class).initializeCustomer();
        context.getBean(SportsclubPopulator.class).initializeSportsclub();
    }

    @Bean
    @Profile("dev")
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods(HttpMethod.PUT.name(),
                                HttpMethod.POST.name(),
                                HttpMethod.DELETE.name(),
                                HttpMethod.GET.name())
                        .allowedOrigins("http://localhost:4200");
            }
        };
    }
}
