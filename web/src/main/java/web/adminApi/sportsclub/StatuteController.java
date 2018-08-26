package web.adminApi.sportsclub;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static query.model.sportsclub.repository.SportsclubQueryExpressions.idMatches;
import static query.model.sportsclub.repository.StatuteQueryExpressions.titleAndSportsclubIdMatches;
import static web.adminApi.sportsclub.dto.StatuteDtoFactory.create;
import static web.common.RequestMappings.DIRECTOR_API_STATUTE;

import java.util.UUID;

import api.sportsclub.command.UpdateStatuteCommand;
import commons.ErrorCode;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import query.model.sportsclub.repository.StatuteEntityRepository;
import web.adminApi.sportsclub.dto.StatuteDto;
import web.common.BaseController;

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
}
