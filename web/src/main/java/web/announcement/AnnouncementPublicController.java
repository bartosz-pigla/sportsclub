package web.announcement;

import static query.model.announcement.repository.AnnouncementQueryExpressions.sportsclubIdMatches;
import static web.common.RequestMappings.PUBLIC_API_ANNOUNCEMENT;

import java.util.UUID;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.announcement.repository.AnnouncementEntityRepository;
import web.announcement.dto.AnnouncementDto;
import web.announcement.dto.AnnouncementDtoFactory;
import web.common.BaseController;

@RestController
@Setter(onMethod_ = { @Autowired })
final class AnnouncementPublicController extends BaseController {

    private AnnouncementEntityRepository announcementRepository;

    @GetMapping(PUBLIC_API_ANNOUNCEMENT)
    @ResponseStatus(HttpStatus.OK)
    Page<AnnouncementDto> get(@PathVariable UUID sportsclubId,
                              @PageableDefault(sort = "lastModificationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return announcementRepository.findAll(sportsclubIdMatches(sportsclubId), pageable)
                .map(AnnouncementDtoFactory::create);
    }
}
