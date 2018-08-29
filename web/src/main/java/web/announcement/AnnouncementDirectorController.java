package web.announcement;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static query.model.announcement.repository.AnnouncementQueryExpressions.titleMatches;
import static web.common.RequestMappings.DIRECTOR_API_ANNOUNCEMENT;
import static web.common.RequestMappings.DIRECTOR_API_SPORTSCLUB_ANNOUNCEMENT_BY_ID;

import java.util.UUID;

import api.sportsclub.command.CreateAnnouncementCommand;
import api.sportsclub.command.DeleteAnnouncementCommand;
import api.sportsclub.command.UpdateAnnouncementCommand;
import commons.ErrorCode;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import query.model.announcement.repository.AnnouncementEntityRepository;
import query.model.announcement.repository.AnnouncementQueryExpressions;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import query.model.sportsclub.repository.SportsclubQueryExpressions;
import web.announcement.dto.AnnouncementDto;
import web.announcement.dto.AnnouncementDtoFactory;
import web.common.BaseController;

@RestController
@Setter(onMethod_ = { @Autowired })
final class AnnouncementDirectorController extends BaseController {

    private SportsclubEntityRepository sportsclubRepository;
    private AnnouncementEntityRepository announcementRepository;

    @PostMapping(DIRECTOR_API_ANNOUNCEMENT)
    public ResponseEntity<?> create(@PathVariable UUID sportsclubId, @RequestBody AnnouncementDto announcementDto) {
        boolean sportsclubExists = sportsclubRepository.exists(
                SportsclubQueryExpressions.idMatches(sportsclubId));

        if (sportsclubExists) {
            String title = announcementDto.getTitle();

            commandGateway.sendAndWait(CreateAnnouncementCommand.builder()
                    .sportsclubId(sportsclubId)
                    .title(title)
                    .content(announcementDto.getContent())
                    .build());

            return announcementRepository.findOne(titleMatches(title))
                    .<ResponseEntity<?>> map(announcement -> ok(AnnouncementDtoFactory.create(announcement)))
                    .orElse(errorResponseService.create("id", ErrorCode.NOT_EXISTS, HttpStatus.CONFLICT));

        } else {
            return badRequest().build();
        }
    }

    @PutMapping(DIRECTOR_API_SPORTSCLUB_ANNOUNCEMENT_BY_ID)
    public ResponseEntity<?> update(@PathVariable UUID sportsclubId,
                                    @PathVariable UUID announcementId,
                                    @RequestBody AnnouncementDto announcementDto) {
        Runnable sendCommand = () -> commandGateway.sendAndWait(UpdateAnnouncementCommand.builder()
                .announcementId(announcementId)
                .sportsclubId(sportsclubId)
                .title(announcementDto.getTitle())
                .content(announcementDto.getContent())
                .build());

        boolean commandSent = sendCommand(sportsclubId, announcementId, sendCommand);
        return commandSent ? noContent().build() : badRequest().build();
    }

    @DeleteMapping(DIRECTOR_API_SPORTSCLUB_ANNOUNCEMENT_BY_ID)
    public ResponseEntity<?> delete(@PathVariable UUID sportsclubId, @PathVariable UUID announcementId) {
        Runnable sendCommand = () -> commandGateway.sendAndWait(
                new DeleteAnnouncementCommand(sportsclubId, announcementId));

        boolean commandSent = sendCommand(sportsclubId, announcementId, sendCommand);
        return commandSent ? noContent().build() : badRequest().build();
    }

    private boolean sendCommand(UUID sportsclubId,
                                UUID announcementId,
                                Runnable sendCommand) {
        boolean sportsclubExists = sportsclubRepository.exists(
                SportsclubQueryExpressions.idMatches(sportsclubId));

        boolean announcementExists = announcementRepository.exists(
                AnnouncementQueryExpressions.idMatches(announcementId));

        if (sportsclubExists && announcementExists) {
            sendCommand.run();
            return true;
        } else {
            return false;
        }
    }
}
