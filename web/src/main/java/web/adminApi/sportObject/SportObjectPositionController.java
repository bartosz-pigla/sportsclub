package web.adminApi.sportObject;

import static web.common.RequestMappings.ADMIN_CONSOLE_SPORT_OBJECT_POSITION;

import java.util.Optional;

import api.sportObject.sportObjectPosition.command.CreateSportObjectPositionCommand;
import domain.sportObject.SportObjectPosition;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import query.model.embeddable.PositiveNumber;
import query.model.sportobject.SportObjectEntity;
import query.model.sportsclub.SportsclubEntity;
import query.repository.SportObjectEntityRepository;
import query.repository.SportObjectPositionEntityRepository;
import query.repository.SportsclubEntityRepository;
import web.adminApi.sportObject.dto.SportObjectPositionDto;
import web.common.BaseController;

@RestController
@Setter(onMethod_ = { @Autowired })
final class SportObjectPositionController extends BaseController {

    private SportsclubEntityRepository sportsclubRepository;
    private SportObjectEntityRepository sportObjectRepository;
    private SportObjectPositionEntityRepository sportObjectPositionRepository;

    @PostMapping(ADMIN_CONSOLE_SPORT_OBJECT_POSITION)
    ResponseEntity<?> createSportObjectPosition(@PathVariable String sportsclubName,
                                                @PathVariable String sportObjectName,
                                                @RequestBody @Validated SportObjectPositionDto sportObjectPositionDto,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findByName(sportsclubName);
        Optional<SportObjectEntity> sportObjectOptional = sportObjectRepository.findByNameAndDeletedFalse(sportObjectName);

        if (sportsclubOptional.isPresent() && sportObjectOptional.isPresent()) {
            SportObjectEntity sportObject = sportObjectOptional.get();
            String positionName = sportObjectPositionDto.getName();
            commandGateway.sendAndWait(CreateSportObjectPositionCommand.builder()
                    .sportObjectId(sportObject.getId())
                    .name(positionName)
                    .description(sportObjectPositionDto.getDescription())
                    .positionsCount(new PositiveNumber(sportObjectPositionDto.getPositionsCount()))
                    .build());
            SportObjectPosition position = sportObjectPositionRepository.findByNameAndDeletedFalse(positionName).get();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
