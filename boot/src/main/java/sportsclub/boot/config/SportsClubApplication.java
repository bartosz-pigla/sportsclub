package sportsclub.boot.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan({
        "sportsclub.web",
        "sportsclub.api",
        "sportsclub.domain"
})
public class SportsClubApplication {
    public static void main(String[] args) {
        SpringApplication.run(SportsClubApplication.class, args);
    }
}
