package web.adminApi.sportObject;

import static web.common.RequestMappings.ADMIN_CONSOLE_SPORT_OBJECT_POSITION;
import static web.common.RequestMappings.ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

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
import query.model.sportobject.SportObjectEntity;
import query.model.sportobject.SportObjectPositionEntity;
import query.model.sportobject.repository.SportObjectEntityRepository;
import query.model.sportobject.repository.SportObjectPositionEntityRepository;
import query.model.sportobject.repository.SportObjectPositionQueryExpressions;
import query.model.sportobject.repository.SportObjectQueryExpressions;
import query.model.sportsclub.SportsclubEntity;
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

    @PostMapping(ADMIN_CONSOLE_SPORT_OBJECT_POSITION)
    ResponseEntity<?> createSportObjectPosition(@PathVariable String sportsclubName,
                                                @PathVariable String sportObjectName,
                                                @RequestBody @Validated SportObjectPositionDto sportObjectPositionDto,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findOne(
                SportsclubQueryExpressions.nameMatches(sportsclubName));

        Optional<SportObjectEntity> sportObjectOptional = sportObjectRepository.findOne(
                SportObjectQueryExpressions.nameMatches(sportObjectName));

        if (sportsclubOptional.isPresent() && sportObjectOptional.isPresent()) {
            SportObjectEntity sportObject = sportObjectOptional.get();
            String positionName = sportObjectPositionDto.getName();
            commandGateway.sendAndWait(CreateSportObjectPositionCommand.builder()
                    .sportObjectId(sportObject.getId())
                    .name(positionName)
                    .description(sportObjectPositionDto.getDescription())
                    .positionsCount(new PositionsCount(sportObjectPositionDto.getPositionsCount()))
                    .build());

            SportObjectPositionEntity positionEntity = sportObjectPositionRepository.findOne(
                    SportObjectPositionQueryExpressions.nameMatches(positionName)).get();
            return ResponseEntity.ok(SportObjectPositionDtoFactory.create(positionEntity));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME)
    ResponseEntity<?> updateSportObjectPosition(@PathVariable String sportsclubName,
                                                @PathVariable String sportObjectName,
                                                @PathVariable String sportObjectPositionName,
                                                @RequestBody @Validated SportObjectPositionDto sportObjectPositionDto,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        return getResponse(sportsclubName, sportObjectName, sportObjectPositionName, (objectId, positionId) -> {
            commandGateway.sendAndWait(UpdateSportObjectPositionCommand.builder()
                    .sportObjectPositionId(positionId)
                    .sportObjectId(objectId)
                    .name(sportObjectPositionDto.getName())
                    .description(sportObjectPositionDto.getDescription())
                    .positionsCount(new PositionsCount(sportObjectPositionDto.getPositionsCount()))
                    .build());

            SportObjectPositionEntity position = sportObjectPositionRepository.findOne(
                    SportObjectPositionQueryExpressions.nameMatches(sportObjectPositionDto.getName())).get();
            return SportObjectPositionDtoFactory.create(position);
        });
    }

    @DeleteMapping(ADMIN_CONSOLE_SPORT_OBJECT_POSITION_BY_NAME)
    ResponseEntity<?> deleteSportObjectPosition(@PathVariable String sportsclubName,
                                                @PathVariable String sportObjectName,
                                                @PathVariable String sportObjectPositionName) {
        return getResponse(sportsclubName, sportObjectName, sportObjectPositionName, (objectId, positionId) -> {
            commandGateway.sendAndWait(new DeleteSportObjectPositionCommand(objectId, positionId));
            SportObjectPositionEntity position = sportObjectPositionRepository.getOne(positionId);
            return SportObjectPositionDtoFactory.create(position);
        });
    }

    private ResponseEntity<?> getResponse(String sportsclubName,
                                          String sportObjectName,
                                          String sportObjectPositionName,
                                          BiFunction<UUID, UUID, SportObjectPositionDto> sendCommand) {
        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findOne(
                SportsclubQueryExpressions.nameMatches(sportsclubName));

        Optional<SportObjectEntity> sportObjectOptional = sportObjectRepository.findOne(
                SportObjectPositionQueryExpressions.nameMatches(sportObjectName));

        Optional<SportObjectPositionEntity> sportObjectPositionOptional = sportObjectPositionRepository.findOne(
                SportObjectPositionQueryExpressions.nameMatches(sportObjectPositionName));

        if (sportsclubOptional.isPresent() && sportObjectOptional.isPresent() && sportObjectPositionOptional.isPresent()) {
            UUID objectId = sportObjectOptional.get().getId();
            UUID positionId = sportObjectPositionOptional.get().getId();
            SportObjectPositionDto responseBody = sendCommand.apply(objectId, positionId);
            return ResponseEntity.ok(responseBody);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SportObjectPositionNameAlreadyExists.class)
    public ResponseEntity<?> handlePositionNameAlreadyExistsConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("name", ErrorCode.ALREADY_EXISTS.getCode()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<?> handleAlreadyDeletedConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("name", ErrorCode.ALREADY_DELETED.getCode()));
    }
}
