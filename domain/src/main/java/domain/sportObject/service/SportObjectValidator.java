package domain.sportObject.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.command.UpdateSportObjectCommand;
import domain.common.exception.AlreadyCreatedException;
import domain.common.exception.AlreadyDeletedException;
import domain.sportObject.SportObject;
import domain.sportObject.exception.NotAssignedToAnySportsclubException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import query.model.sportobject.repository.SportObjectEntityRepository;
import query.model.sportobject.repository.SportObjectQueryExpressions;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import query.model.sportsclub.repository.SportsclubQueryExpressions;

@Service
@AllArgsConstructor
public final class SportObjectValidator {

    private static final Logger logger = getLogger(SportObjectValidator.class);

    private SportObjectEntityRepository sportObjectRepository;
    private SportsclubEntityRepository sportsclubRepository;

    public void validate(CreateSportObjectCommand command) {
        String sportObjectName = command.getName();
        assertThatNameIsUnique(sportObjectName);
        assertThatIsAssignedToExistingSportsclub(command.getSportsclubId(), sportObjectName);
    }

    public void validate(UpdateSportObjectCommand command, SportObject sportObject) {
        String sportObjectName = command.getName();
        assertThatIsNotDeleted(sportObject.isDeleted(), sportObjectName);

        if (sportObjectRepository.exists(
                SportObjectQueryExpressions.nameMatchesWithIdOtherThan(sportObjectName, sportObject.getSportObjectId()))) {
            logger.error("Sport object with name {} already exists", sportObjectName);
            throw new AlreadyCreatedException();
        }

        assertThatIsAssignedToExistingSportsclub(command.getSportsclubId(), sportObjectName);
    }

    public void assertThatIsNotDeleted(boolean deleted, String sportObjectName) {
        if (deleted) {
            logger.error("Sport object with name {} is already deleted", sportObjectName);
            throw new AlreadyDeletedException();
        }
    }

    private void assertThatNameIsUnique(String sportObjectName) {
        if (sportObjectRepository.exists(SportObjectQueryExpressions.nameMatches(sportObjectName))) {
            logger.error("Sport object with name {} already exists", sportObjectName);
            throw new AlreadyCreatedException();
        }
    }

    private void assertThatIsAssignedToExistingSportsclub(UUID sportsclubId, String sportObjectName) {
        if (!sportsclubRepository.exists(SportsclubQueryExpressions.idMatches(sportsclubId))) {
            logger.error("Sport object with name {} is not assigned to any sportsclub", sportObjectName);
            throw new NotAssignedToAnySportsclubException();
        }
    }
}
