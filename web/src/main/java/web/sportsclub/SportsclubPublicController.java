package web.sportsclub;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static query.model.sportsclub.repository.SportsclubQueryExpressions.idMatches;
import static web.common.RequestMappings.PUBLIC_API_SPORTSCLUB_BY_ID;

import java.util.UUID;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import web.common.BaseController;
import web.sportsclub.dto.SportsclubDtoFactory;

@RestController
@Setter(onMethod_ = { @Autowired })
final class SportsclubPublicController extends BaseController {

    private SportsclubEntityRepository sportsclubRepository;

    @GetMapping(PUBLIC_API_SPORTSCLUB_BY_ID)
    ResponseEntity<?> get(@PathVariable UUID sportsclubId) {
        return sportsclubRepository.findOne(idMatches(sportsclubId))
                .map(sportsclub -> ok(SportsclubDtoFactory.create(sportsclub)))
                .orElse(badRequest().build());
    }
}
