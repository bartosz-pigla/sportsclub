package web.adminApi.announcement;

import static web.common.RequestMappings.ADMIN_CONSOLE_ANNOUNCEMENT;
import static web.common.RequestMappings.ADMIN_CONSOLE_SPORTSCLUB_ANNOUNCEMENT_BY_ID;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

import api.sportsclub.command.CreateAnnouncementCommand;
import api.sportsclub.command.DeleteAnnouncementCommand;
import api.sportsclub.command.UpdateAnnouncementCommand;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import query.model.announcement.AnnouncementEntity;
import query.model.sportsclub.SportsclubEntity;
import query.repository.AnnouncementEntityRepository;
import query.repository.SportsclubEntityRepository;
import web.adminApi.announcement.dto.AnnouncementDto;
import web.common.BaseController;

@RestController
@Setter(onMethod_ = { @Autowired })
final class AnnouncementController extends BaseController {

    private SportsclubEntityRepository sportsclubRepository;
    private AnnouncementEntityRepository announcementRepository;

    @PostMapping(ADMIN_CONSOLE_ANNOUNCEMENT)
    public ResponseEntity<?> createAnnouncement(@PathVariable String sportsclubName, @RequestBody AnnouncementDto announcement) {
        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findByName(sportsclubName);

        if (sportsclubOptional.isPresent()) {
            SportsclubEntity sportsclub = sportsclubOptional.get();
            commandGateway.sendAndWait(CreateAnnouncementCommand.builder()
                    .sportsclubId(sportsclub.getId())
                    .title(announcement.getTitle())
                    .content(announcement.getContent()).build());
            return ResponseEntity.ok(announcement);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(ADMIN_CONSOLE_SPORTSCLUB_ANNOUNCEMENT_BY_ID)
    public ResponseEntity<?> updateAnnouncement(@PathVariable String sportsclubName, @PathVariable String id, @RequestBody AnnouncementDto announcementDto) {
        return getResponse(sportsclubName, id, (sportsclub, announcement) ->
                commandGateway.sendAndWait(UpdateAnnouncementCommand.builder()
                        .announcementId(announcement.getId())
                        .sportsclubId(sportsclub.getId())
                        .title(announcementDto.getTitle())
                        .content(announcementDto.getContent()).build()));
    }

    @DeleteMapping(ADMIN_CONSOLE_SPORTSCLUB_ANNOUNCEMENT_BY_ID)
    public ResponseEntity<?> deleteAnnouncement(@PathVariable String sportsclubName, @PathVariable String id) {
        return getResponse(sportsclubName, id, (sportsclub, announcement) ->
                commandGateway.sendAndWait(new DeleteAnnouncementCommand(sportsclub.getId(), announcement.getId())));
    }

    private ResponseEntity<?> getResponse(String sportsclubName, String announcementId, BiConsumer<SportsclubEntity, AnnouncementEntity> sendCommand) {
        if (isInvalidUUID(announcementId)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findByName(sportsclubName);
        Optional<AnnouncementEntity> announcementOptional = announcementRepository.findById(UUID.fromString(announcementId));

        if (sportsclubOptional.isPresent() && announcementOptional.isPresent()) {
            SportsclubEntity sportsclub = sportsclubOptional.get();
            AnnouncementEntity announcement = announcementOptional.get();
            sendCommand.accept(sportsclub, announcement);
            announcement = announcementRepository.getOne(UUID.fromString(announcementId));
            return ResponseEntity.ok(AnnouncementDto.builder()
                    .id(announcementId)
                    .title(announcement.getTitle())
                    .content(announcement.getContent())
                    .lastModificationDate(announcement.getLastModificationDate()).build());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
