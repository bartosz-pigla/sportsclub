package web.adminApi.sportObject;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static web.common.RequestMappings.DIRECTOR_API_SPORT_OBJECT_POSITION;
import static web.common.RequestMappings.DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID;

import java.util.List;
import java.util.UUID;

import api.sportObject.sportObjectPosition.command.CreateSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.command.DeleteSportObjectPositionCommand;
import api.sportObject.sportObjectPosition.command.UpdateSportObjectPositionCommand;
import commons.ErrorCode;
import domain.common.exception.AlreadyDeletedException;
import domain.sportObject.exception.SportObjectPositionNameAlreadyExists;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.embeddable.PositionsCount;
import query.model.sportobject.repository.SportObjectEntityRepository;
import query.model.sportobject.repository.SportObjectPositionEntityRepository;
import query.model.sportobject.repository.SportObjectPositionQueryExpressions;
import query.model.sportobject.repository.SportObjectQueryExpressions;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import query.model.sportsclub.repository.SportsclubQueryExpressions;
import web.adminApi.sportObject.dto.SportObjectPositionDto;
import web.adminApi.sportObject.dto.SportObjectPositionDtoFactory;
import web.adminApi.sportObject.service.SportObjectPositionDtoValidator;
import web.common.BaseController;
import web.common.dto.FieldErrorDto;

@RestController
@Setter(onMethod_ = { @Autowired })
final class SportObjectPositionController extends BaseController {

    private SportsclubEntityRepository sportsclubRepository;
    private SportObjectEntityRepository sportObjectRepository;
    private SportObjectPositionEntityRepository sportObjectPositionRepository;
    private SportObjectPositionDtoValidator validator;

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(DIRECTOR_API_SPORT_OBJECT_POSITION)
    ResponseEntity<?> create(@PathVariable UUID sportsclubId,
                             @PathVariable UUID sportObjectId,
                             @RequestBody @Validated SportObjectPositionDto sportObjectPositionDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorResponseService.create(bindingResult);
        }

        boolean sportsclubExists = sportsclubRepository.exists(
                SportsclubQueryExpressions.idMatches(sportsclubId));

        boolean sportObjectExists = sportObjectRepository.exists(
                SportObjectQueryExpressions.idMatches(sportObjectId));

        if (sportsclubExists && sportObjectExists) {
            String positionName = sportObjectPositionDto.getName();

            commandGateway.sendAndWait(CreateSportObjectPositionCommand.builder()
                    .sportObjectId(sportObjectId)
                    .name(positionName)
                    .description(sportObjectPositionDto.getDescription())
                    .positionsCount(new PositionsCount(sportObjectPositionDto.getPositionsCount()))
                    .build());

            return sportObjectPositionRepository.findOne(SportObjectPositionQueryExpressions.nameMatches(positionName))
                    .<ResponseEntity<?>> map(position -> ok(SportObjectPositionDtoFactory.create(position)))
                    .orElse(errorResponseService.create("name", ErrorCode.NOT_EXISTS, HttpStatus.CONFLICT));
        } else {
            return badRequest().build();
        }
    }

    @PutMapping(DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID)
    ResponseEntity<?> update(@PathVariable UUID sportsclubId,
                             @PathVariable UUID sportObjectId,
                             @PathVariable UUID sportObjectPositionId,
                             @RequestBody @Validated SportObjectPositionDto sportObjectPositionDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorResponseService.create(bindingResult);
        }

        Runnable sendCommand = () -> commandGateway.sendAndWait(UpdateSportObjectPositionCommand.builder()
                .sportObjectPositionId(sportObjectPositionId)
                .sportObjectId(sportObjectId)
                .name(sportObjectPositionDto.getName())
                .description(sportObjectPositionDto.getDescription())
                .positionsCount(new PositionsCount(sportObjectPositionDto.getPositionsCount()))
                .build());

        boolean commandSent = sendCommand(sportsclubId, sportObjectId, sportObjectPositionId, sendCommand);
        return commandSent ? noContent().build() : badRequest().build();
    }

    @DeleteMapping(DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID)
    ResponseEntity<?> delete(@PathVariable UUID sportsclubId,
                             @PathVariable UUID sportObjectId,
                             @PathVariable UUID sportObjectPositionId) {
        Runnable sendCommand = () -> commandGateway.sendAndWait(
                new DeleteSportObjectPositionCommand(sportObjectId, sportObjectPositionId));

        boolean commandSent = sendCommand(sportsclubId, sportObjectId, sportObjectPositionId, sendCommand);
        return commandSent ? noContent().build() : badRequest().build();
    }

    private boolean sendCommand(UUID sportsclubId,
                                UUID sportObjectId,
                                UUID sportObjectPositionId,
                                Runnable sendCommand) {
        boolean sportsclubExists = sportsclubRepository.exists(
                SportsclubQueryExpressions.idMatches(sportsclubId));

        boolean sportObjectExists = sportObjectRepository.exists(
                SportObjectQueryExpressions.idMatches(sportObjectId));

        boolean sportObjectPositionExists = sportObjectPositionRepository.exists(
                SportObjectPositionQueryExpressions.idMatches(sportObjectPositionId));

        if (sportsclubExists && sportObjectExists && sportObjectPositionExists) {
            sendCommand.run();
            return true;
        } else {
            return false;
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SportObjectPositionNameAlreadyExists.class)
    public List<FieldErrorDto> handlePositionNameAlreadyExistsConflict() {
        return errorResponseService.createBody("name", ErrorCode.ALREADY_EXISTS);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyDeletedException.class)
    public List<FieldErrorDto> handleAlreadyDeletedConflict() {
        return errorResponseService.createBody("id", ErrorCode.ALREADY_DELETED);
    }
}
