package web.adminApi.sportsclub;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static query.model.sportsclub.repository.SportsclubQueryExpressions.nameMatches;
import static web.common.RequestMappings.ADMIN_API_STATUTE;

import java.util.Optional;

import api.sportsclub.command.UpdateStatuteCommand;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import query.model.sportsclub.SportsclubEntity;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import web.adminApi.sportsclub.dto.StatuteDto;
import web.common.BaseController;

@RestController
@Setter(onMethod_ = { @Autowired })
final class StatuteController extends BaseController {

    private SportsclubEntityRepository sportsclubRepository;

    @PostMapping(ADMIN_API_STATUTE)
    ResponseEntity<?> createOrUpdateStatute(@PathVariable String sportsclubName, @RequestBody StatuteDto statute) {
        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findOne(
                nameMatches(sportsclubName));
        if (sportsclubOptional.isPresent()) {
            SportsclubEntity sportsclub = sportsclubOptional.get();
            commandGateway.sendAndWait(UpdateStatuteCommand.builder()
                    .sportsclubId(sportsclub.getId())
                    .title(statute.getTitle())
                    .description(statute.getDescription()).build());
            return ok(statute);
        } else {
            return badRequest().build();
        }
    }
}
