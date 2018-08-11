package web.adminApi.sportObject;

import static web.common.RequestMappings.ADMIN_CONSOLE_OPENING_TIME;
import static web.common.RequestMappings.ADMIN_CONSOLE_OPENING_TIME_BY_ID;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

import api.sportObject.openingTime.command.CreateOpeningTimeCommand;
import api.sportObject.openingTime.command.DeleteOpeningTimeCommand;
import api.sportObject.openingTime.command.UpdateOpeningTimeCommand;
import commons.ErrorCode;
import domain.common.exception.AlreadyDeletedException;
import domain.sportObject.exception.OpeningTimeRangeConflictException;
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
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.Price;
import query.model.sportobject.OpeningTimeEntity;
import query.model.sportobject.SportObjectEntity;
import query.model.sportsclub.SportsclubEntity;
import query.repository.OpeningTimeEntityRepository;
import query.repository.SportObjectEntityRepository;
import query.repository.SportsclubEntityRepository;
import web.adminApi.sportObject.dto.OpeningTimeDto;
import web.adminApi.sportObject.dto.OpeningTimeRangeDto;
import web.adminApi.sportObject.dto.OpeningTimeRangeDtoFactory;
import web.adminApi.sportObject.service.OpeningTimeRangeDtoValidator;
import web.common.BaseController;
import web.common.dto.FieldErrorDto;

@RestController
@Setter(onMethod_ = { @Autowired })
final class OpeningTimeController extends BaseController {

    private OpeningTimeRangeDtoValidator validator;
    private SportsclubEntityRepository sportsclubRepository;
    private SportObjectEntityRepository sportObjectRepository;
    private OpeningTimeEntityRepository openingTimeRepository;

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(ADMIN_CONSOLE_OPENING_TIME)
    ResponseEntity<?> createOpeningHours(@PathVariable String sportsclubName,
                                         @PathVariable String sportObjectName,
                                         @RequestBody @Validated OpeningTimeRangeDto openingTimeRangeDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findByName(sportsclubName);
        Optional<SportObjectEntity> sportObjectOptional = sportObjectRepository.findByNameAndDeletedFalse(sportObjectName);

        if (sportsclubOptional.isPresent() && sportObjectOptional.isPresent()) {
            UUID sportObjectId = sportObjectOptional.get().getId();
            OpeningTimeDto startTime = openingTimeRangeDto.getStartTime();
            OpeningTimeDto finishTime = openingTimeRangeDto.getFinishTime();
            commandGateway.sendAndWait(CreateOpeningTimeCommand.builder()
                    .sportObjectId(sportObjectId)
                    .price(new Price(new BigDecimal(openingTimeRangeDto.getPrice())))
                    .dateRange(new OpeningTimeRange(openingTimeRangeDto.getDayOfWeek(),
                            LocalTime.of(startTime.getHour(), startTime.getMinute()),
                            LocalTime.of(finishTime.getHour(), finishTime.getMinute()))).build());
            return ResponseEntity.ok(openingTimeRangeDto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(ADMIN_CONSOLE_OPENING_TIME_BY_ID)
    ResponseEntity<?> updateOpeningHours(@PathVariable String sportsclubName,
                                         @PathVariable String sportObjectName,
                                         @PathVariable String openingTimeId,
                                         @RequestBody @Validated OpeningTimeRangeDto openingTimeRangeDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        } else if (isInvalidUUID(openingTimeId)) {
            return ResponseEntity.badRequest().build();
        }

        OpeningTimeDto startTime = openingTimeRangeDto.getStartTime();
        OpeningTimeDto finishTime = openingTimeRangeDto.getFinishTime();

        return getResponse(sportsclubName, sportObjectName, openingTimeId, (sportObject, openingTime) ->
                commandGateway.sendAndWait(UpdateOpeningTimeCommand.builder()
                        .openingTimeId(openingTime.getId())
                        .sportObjectId(sportObject.getId())
                        .dateRange(new OpeningTimeRange(
                                openingTimeRangeDto.getDayOfWeek(),
                                LocalTime.of(startTime.getHour(), startTime.getMinute()),
                                LocalTime.of(finishTime.getHour(), finishTime.getMinute())))
                        .price(new Price(new BigDecimal(openingTimeRangeDto.getPrice()))).build()));
    }

    @DeleteMapping(ADMIN_CONSOLE_OPENING_TIME_BY_ID)
    ResponseEntity<?> deleteOpeningHours(@PathVariable String sportsclubName,
                                         @PathVariable String sportObjectName,
                                         @PathVariable String openingTimeId) {
        if (isInvalidUUID(openingTimeId)) {
            return ResponseEntity.badRequest().build();
        }

        return getResponse(sportsclubName, sportObjectName, openingTimeId, (sportObject, openingTime) ->
                commandGateway.sendAndWait(DeleteOpeningTimeCommand.builder()
                        .openingTimeId(openingTime.getId())
                        .sportObjectId(sportObject.getId()).build()));
    }

    private ResponseEntity<?> getResponse(String sportsclubName,
                                          String sportObjectName,
                                          String openingTimeId,
                                          BiConsumer<SportObjectEntity, OpeningTimeEntity> sendCommand) {
        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findByName(sportsclubName);
        Optional<SportObjectEntity> sportObjectOptional = sportObjectRepository.findByNameAndDeletedFalse(sportObjectName);
        Optional<OpeningTimeEntity> openingTimeOptional = openingTimeRepository.findByIdAndDeletedFalse(UUID.fromString(openingTimeId));

        if (sportsclubOptional.isPresent() && sportObjectOptional.isPresent() && openingTimeOptional.isPresent()) {
            sendCommand.accept(sportObjectOptional.get(), openingTimeOptional.get());
            return ResponseEntity.ok(OpeningTimeRangeDtoFactory.create(openingTimeRepository.getOne(UUID.fromString(openingTimeId))));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OpeningTimeRangeConflictException.class)
    public ResponseEntity<?> handleOpeningTimeRangeConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("openingTimeRange", ErrorCode.ALREADY_EXISTS.getCode()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<?> handleAlreadyDeletedConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("openingTimeRange", ErrorCode.ALREADY_DELETED.getCode()));
    }
}
