package boot.populator;

import javax.annotation.PostConstruct;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import query.repository.SportsclubEntityRepository;

@Service
@AllArgsConstructor
final class SportsclubPopulator {

    private SportsclubEntityRepository sportsclubRepository;

    @PostConstruct
    public void initializeSportsclub() {
        if (!sportsclubRepository.existsByName("sportsclub")) {

        }
    }
}
