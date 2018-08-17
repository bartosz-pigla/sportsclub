package web.adminApi.sportObject;

import static java.util.UUID.fromString;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static query.model.sportsclub.repository.SportsclubQueryExpressions.nameMatches;
import static web.common.RequestMappings.ADMIN_API_OPENING_TIME;
import static web.common.RequestMappings.ADMIN_API_OPENING_TIME_BY_ID;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

import api.sportObject.openingTime.command.CreateOpeningTimeCommand;
import api.sportObject.openingTime.command.DeleteOpeningTimeCommand;
import api.sportObject.openingTime.command.UpdateOpeningTimeCommand;
import com.google.common.collect.ImmutableList;
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
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import query.model.sportobject.repository.OpeningTimeQueryExpressions;
import query.model.sportobject.repository.SportObjectEntityRepository;
import query.model.sportobject.repository.SportObjectQueryExpressions;
import query.model.sportsclub.SportsclubEntity;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import query.model.sportsclub.repository.SportsclubQueryExpressions;
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

    @PostMapping(ADMIN_API_OPENING_TIME)
    ResponseEntity<?> createOpeningHours(@PathVariable String sportsclubName,
                                         @PathVariable String sportObjectName,
                                         @RequestBody @Validated OpeningTimeRangeDto openingTimeRangeDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findOne(
                SportsclubQueryExpressions.nameMatches(sportsclubName));

        Optional<SportObjectEntity> sportObjectOptional = sportObjectRepository.findOne(
                SportObjectQueryExpressions.nameMatches(sportObjectName));

        if (sportsclubOptional.isPresent() && sportObjectOptional.isPresent()) {
            UUID sportObjectId = sportObjectOptional.get().getId();
            OpeningTimeDto startTime = openingTimeRangeDto.getStartTime();
            OpeningTimeDto finishTime = openingTimeRangeDto.getFinishTime();
            commandGateway.sendAndWait(CreateOpeningTimeCommand.builder()
                    .sportObjectId(sportObjectId)
                    .price(new Price(new BigDecimal(openingTimeRangeDto.getPrice())))
                    .timeRange(new OpeningTimeRange(openingTimeRangeDto.getDayOfWeek(),
                            LocalTime.of(startTime.getHour(), startTime.getMinute()),
                            LocalTime.of(finishTime.getHour(), finishTime.getMinute()))).build());
            return ok(openingTimeRangeDto);
        } else {
            return badRequest().build();
        }
    }

    @PutMapping(ADMIN_API_OPENING_TIME_BY_ID)
    ResponseEntity<?> updateOpeningHours(@PathVariable String sportsclubName,
                                         @PathVariable String sportObjectName,
                                         @PathVariable String openingTimeId,
                                         @RequestBody @Validated OpeningTimeRangeDto openingTimeRangeDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        } else if (isInvalidUUID(openingTimeId)) {
            return badRequest().build();
        }

        OpeningTimeDto startTime = openingTimeRangeDto.getStartTime();
        OpeningTimeDto finishTime = openingTimeRangeDto.getFinishTime();

        return getResponse(sportsclubName, sportObjectName, openingTimeId, (sportObject, openingTime) ->
                commandGateway.sendAndWait(UpdateOpeningTimeCommand.builder()
                        .openingTimeId(openingTime.getId())
                        .sportObjectId(sportObject.getId())
                        .timeRange(new OpeningTimeRange(
                                openingTimeRangeDto.getDayOfWeek(),
                                LocalTime.of(startTime.getHour(), startTime.getMinute()),
                                LocalTime.of(finishTime.getHour(), finishTime.getMinute())))
                        .price(new Price(new BigDecimal(openingTimeRangeDto.getPrice()))).build()));
    }

    @DeleteMapping(ADMIN_API_OPENING_TIME_BY_ID)
    ResponseEntity<?> deleteOpeningHours(@PathVariable String sportsclubName,
                                         @PathVariable String sportObjectName,
                                         @PathVariable String openingTimeId) {
        if (isInvalidUUID(openingTimeId)) {
            return badRequest().build();
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
        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findOne(nameMatches(sportsclubName));

        Optional<SportObjectEntity> sportObjectOptional = sportObjectRepository.findOne(
                SportObjectQueryExpressions.nameMatches(sportObjectName));

        Optional<OpeningTimeEntity> openingTimeOptional = openingTimeRepository.findOne(
                OpeningTimeQueryExpressions.idMatches(fromString(openingTimeId)));

        if (sportsclubOptional.isPresent() && sportObjectOptional.isPresent() && openingTimeOptional.isPresent()) {
            sendCommand.accept(sportObjectOptional.get(), openingTimeOptional.get());
            return ok(OpeningTimeRangeDtoFactory.create(openingTimeRepository.getOne(fromString(openingTimeId))));
        } else {
            return badRequest().build();
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OpeningTimeRangeConflictException.class)
    public List<FieldErrorDto> handleOpeningTimeRangeConflict() {
        return ImmutableList.of(new FieldErrorDto("openingTimeRange", ErrorCode.ALREADY_EXISTS));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyDeletedException.class)
    public List<FieldErrorDto> handleAlreadyDeletedConflict() {
        return ImmutableList.of(new FieldErrorDto("openingTimeRange", ErrorCode.ALREADY_DELETED));
    }
}
