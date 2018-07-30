package domain.sportsclub.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.UUID;

import domain.common.exception.NotExistsException;
import domain.sportsclub.Sportsclub;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class AnnouncementValidator {

    private static final Logger logger = getLogger(AnnouncementValidator.class);

    public void validate(UUID announcementId, Sportsclub sportsclub) {
        if (!sportsclub.getAnnouncementIds().contains(announcementId)) {
            logger.error("Announcement with id: {} not exists in sportsclub with id: {}", announcementId, sportsclub.getSportsclubId());
            throw new NotExistsException();
        }
    }
}
