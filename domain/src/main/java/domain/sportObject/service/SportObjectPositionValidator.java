package domain.sportObject.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Optional;
import java.util.UUID;

import api.sportObject.sportObjectPosition.command.CreateSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.command.DeleteSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.command.UpdateSportObjectPositionCommand;
import domain.common.exception.AlreadyDeletedException;
import domain.common.exception.NotExistsException;
import domain.sportObject.SportObject;
import domain.sportObject.SportObjectPosition;
import domain.sportObject.exception.SportObjectPositionNameAlreadyExists;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class SportObjectPositionValidator {

    private static final Logger logger = getLogger(SportObjectPositionValidator.class);

    public void validate(CreateSportObjectPositionCommand command, SportObject sportObject) {
        sportObject.findPositionByName(command.getName()).ifPresent(position -> throwNameAlreadyExists(position.getSportObjectPositionId()));
    }

    public void validate(DeleteSportObjectPositionCommand command, SportObject sportObject) {
        UUID positionId = command.getSportObjectPositionId();
        Optional<SportObjectPosition> positionOptional = sportObject.findPositionById(positionId);

        if (positionOptional.isPresent()) {
            SportObjectPosition position = positionOptional.get();

            if (position.isDeleted()) {
                throwAlreadyDeletedException(positionId);
            }
        } else {
            throwNotExistsException(positionId);
        }
    }

    public void validate(UpdateSportObjectPositionCommand command, SportObject sportObject) {
        UUID positionId = command.getSportObjectPositionId();
        Optional<SportObjectPosition> positionOptional = sportObject.findPositionById(positionId);

        if (positionOptional.isPresent()) {
            SportObjectPosition position = positionOptional.get();

            if (position.isDeleted()) {
                throwAlreadyDeletedException(positionId);
            } else if (sportObject.findPositionByNameExcept(command.getName(), position).isPresent()) {
                throwNameAlreadyExists(positionId);
            }
        } else {
            throwNotExistsException(positionId);
        }
    }

    private void throwAlreadyDeletedException(UUID positionId) {
        logger.error("Sport object position with id: {} is already deleted", positionId);
        throw new AlreadyDeletedException();
    }

    private void throwNotExistsException(UUID positionId) {
        logger.error("Sport object position with id: {} not exists", positionId);
        throw new NotExistsException();
    }

    private void throwNameAlreadyExists(UUID positionId) {
        logger.error("Sport object position with id: {} has the same name", positionId);
        throw new SportObjectPositionNameAlreadyExists();
    }
}
