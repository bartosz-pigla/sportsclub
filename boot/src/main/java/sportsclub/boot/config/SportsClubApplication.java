package sportsclub.boot.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan({
        "sportsclub.web",
        "sportsclub.api",
        "sportsclub.domain",
        "sportsclub.boot.config"
})
@EnableJpaRepositories(value = {
        "sportsclub.domain",
        "org.axonframework"
})
@EntityScan(value = {
        "sportsclub.domain",
        "org.axonframework"
})
public class SportsClubApplication {
    public static void main(String[] args) {
        SpringApplication.run(SportsClubApplication.class, args);
    }
}
