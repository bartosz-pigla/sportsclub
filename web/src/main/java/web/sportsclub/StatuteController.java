package web.sportsclub;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static query.model.sportsclub.repository.SportsclubQueryExpressions.idMatches;
import static query.model.sportsclub.repository.StatuteQueryExpressions.sportsclubIdMatches;
import static query.model.sportsclub.repository.StatuteQueryExpressions.titleAndSportsclubIdMatches;
import static web.common.RequestMappings.DIRECTOR_API_STATUTE;
import static web.common.RequestMappings.PUBLIC_API_STATUTE;
import static web.sportsclub.dto.StatuteDtoFactory.create;

import java.util.UUID;

import api.sportsclub.command.UpdateStatuteCommand;
import commons.ErrorCode;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import query.model.sportsclub.repository.StatuteEntityRepository;
import web.common.BaseController;
import web.sportsclub.dto.StatuteDto;

@RestController
@Setter(onMethod_ = { @Autowired })
final class StatuteController extends BaseController {

    private SportsclubEntityRepository sportsclubRepository;
    private StatuteEntityRepository statuteRepository;

    @PostMapping(DIRECTOR_API_STATUTE)
    ResponseEntity<?> createOrUpdate(@PathVariable UUID sportsclubId, @RequestBody StatuteDto statuteDto) {
        boolean sportsclubExists = sportsclubRepository.exists(idMatches(sportsclubId));

        if (sportsclubExists) {
            String title = statuteDto.getTitle();

            commandGateway.sendAndWait(UpdateStatuteCommand.builder()
                    .sportsclubId(sportsclubId)
                    .title(title)
                    .description(statuteDto.getDescription())
                    .build());

            return statuteRepository.findOne(titleAndSportsclubIdMatches(title, sportsclubId))
                    .<ResponseEntity<?>> map(statute -> ok(create(statute)))
                    .orElse(errorResponseService.create("title", ErrorCode.NOT_EXISTS, HttpStatus.CONFLICT));
        } else {
            return badRequest().build();
        }
    }

    @GetMapping(PUBLIC_API_STATUTE)
    ResponseEntity<?> get(@PathVariable UUID sportsclubId) {
        return statuteRepository.findOne(sportsclubIdMatches(sportsclubId))
                .map(statuteEntity -> ok(create(statuteEntity))).orElseGet(() -> ok().build());
    }
}
