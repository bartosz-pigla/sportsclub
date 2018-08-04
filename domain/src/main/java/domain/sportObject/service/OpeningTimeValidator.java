package domain.sportObject.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import api.sportObject.command.CreateOpeningTimeCommand;
import api.sportObject.command.DeleteOpeningTimeCommand;
import api.sportObject.command.UpdateOpeningTimeCommand;
import domain.common.exception.AlreadyDeletedException;
import domain.common.exception.NotExistsException;
import domain.sportObject.OpeningTime;
import domain.sportObject.SportObject;
import domain.sportObject.exception.OpeningTimeRangeConflictException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import query.model.embeddable.OpeningTimeRange;

@Service
@AllArgsConstructor
public final class OpeningTimeValidator {

    private static final Logger logger = getLogger(SportObjectValidator.class);

    public void validate(CreateOpeningTimeCommand command, SportObject sportObject) {
        assertThatSportObjectIsNotDeleted(sportObject);
        OpeningTimeRange newTimeRange = command.getDateRange();
        sportObject.getOpeningHours().stream()
                .filter(o -> !o.isDeleted() && o.getOpeningTimeRange().contains(newTimeRange)).findFirst()
                .ifPresent(timeRange -> {
                    logger.error("Opening time range {} conflicts with existing range {}", newTimeRange, timeRange);
                    throw new OpeningTimeRangeConflictException();
                });
    }

    public void validate(UpdateOpeningTimeCommand command, SportObject sportObject) {
        assertThatSportObjectIsNotDeleted(sportObject);

        Collection<OpeningTime> existingOpeningHours = sportObject.getOpeningHours();
        OpeningTimeRange newTimeRange = command.getDateRange();
        Optional<OpeningTime> openingTimeOptional = existingOpeningHours.stream()
                .filter(o -> !o.isDeleted() && o.getOpeningTimeId().equals(command.getOpeningTimeId())).findFirst();

        if (openingTimeOptional.isPresent()) {
            UUID dateRangeId = openingTimeOptional.get().getOpeningTimeId();
            existingOpeningHours.stream()
                    .filter(o -> !o.isDeleted() && !o.getOpeningTimeId().equals(dateRangeId) && o.getOpeningTimeRange().contains(newTimeRange))
                    .findFirst().ifPresent(o -> {
                logger.error("Opening time range {} conflicts with existing range {}", newTimeRange, o);
                throw new OpeningTimeRangeConflictException();
            });
        } else {
            logger.error("Opening time with id: {} not exists", command.getOpeningTimeId());
            throw new NotExistsException();
        }
    }

    public void validate(DeleteOpeningTimeCommand command, SportObject sportObject) {
        assertThatSportObjectIsNotDeleted(sportObject);

        Optional<OpeningTime> openingTimeOptional = sportObject.getOpeningHours().stream()
                .filter(o -> o.getOpeningTimeId().equals(command.getOpeningTimeId())).findFirst();

        if (openingTimeOptional.isPresent() && openingTimeOptional.get().isDeleted()) {
            logger.error("Opening time with id {} is already deleted", command.getOpeningTimeId());
            throw new AlreadyDeletedException();
        } else if (!openingTimeOptional.isPresent()) {
            logger.error("Opening time with id {} not exists", command.getOpeningTimeId());
            throw new NotExistsException();
        }
    }

    public void assertThatSportObjectIsNotDeleted(SportObject sportObject) {
        if (sportObject.isDeleted()) {
            logger.error("Sport object with id {} is already deleted", sportObject.getSportObjectId());
            throw new AlreadyDeletedException();
        }
    }
}
