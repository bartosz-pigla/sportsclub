package domain.sportsclub.service;

import static org.slf4j.LoggerFactory.getLogger;
import static query.model.sportsclub.repository.SportsclubQueryExpressions.nameMatches;

import api.sportsclub.command.CreateSportsclubCommand;
import domain.common.exception.AlreadyCreatedException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import query.model.sportsclub.repository.SportsclubEntityRepository;

@Service
@AllArgsConstructor
public final class CreateSportsclubValidator {

    private static final Logger logger = getLogger(CreateSportsclubValidator.class);

    private SportsclubEntityRepository sportsclubRepository;

    public void validate(CreateSportsclubCommand command) {
        if (sportsclubRepository.exists(nameMatches(command.getName()))) {
            logger.error("Sportsclub already exists with name: {}", command.getName());
            throw new AlreadyCreatedException();
        }
    }
}
