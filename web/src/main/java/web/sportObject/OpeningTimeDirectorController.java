package web.sportObject;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Comparator.comparing;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static web.common.RequestMappings.DIRECTOR_API_DAY_OPENING_TIME;
import static web.common.RequestMappings.DIRECTOR_API_OPENING_TIME;
import static web.common.RequestMappings.DIRECTOR_API_OPENING_TIME_BY_ID;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.GetMapping;
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
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import query.model.sportobject.repository.OpeningTimeQueryExpressions;
import query.model.sportobject.repository.SportObjectEntityRepository;
import query.model.sportobject.repository.SportObjectQueryExpressions;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import query.model.sportsclub.repository.SportsclubQueryExpressions;
import web.common.BaseController;
import web.common.dto.FieldErrorDto;
import web.sportObject.dto.DayOpeningTimeDto;
import web.sportObject.dto.DayOpeningTimeDtoFactory;
import web.sportObject.dto.OpeningTimeDto;
import web.sportObject.dto.OpeningTimeDtoFactory;
import web.sportObject.dto.TimeDto;
import web.sportObject.service.OpeningTimeRangeDtoValidator;

@RestController
@Setter(onMethod_ = { @Autowired })
final class OpeningTimeDirectorController extends BaseController {

    private OpeningTimeRangeDtoValidator validator;
    private SportsclubEntityRepository sportsclubRepository;
    private SportObjectEntityRepository sportObjectRepository;
    private OpeningTimeEntityRepository openingTimeRepository;

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(DIRECTOR_API_OPENING_TIME)
    ResponseEntity<?> create(@PathVariable UUID sportsclubId,
                             @PathVariable UUID sportObjectId,
                             @RequestBody @Validated OpeningTimeDto openingTimeDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorResponseService.create(bindingResult);
        }

        boolean sportsclubExists = sportsclubRepository.exists(
                SportsclubQueryExpressions.idMatches(sportsclubId));

        boolean sportObjectExists = sportObjectRepository.exists(
                SportObjectQueryExpressions.idMatches(sportObjectId));

        if (sportsclubExists && sportObjectExists) {
            TimeDto startTime = openingTimeDto.getStartTime();
            TimeDto finishTime = openingTimeDto.getFinishTime();

            OpeningTimeRange timeRange = new OpeningTimeRange(openingTimeDto.getDayOfWeek(),
                    LocalTime.of(startTime.getHour(), startTime.getMinute()),
                    LocalTime.of(finishTime.getHour(), finishTime.getMinute()));

            commandGateway.sendAndWait(CreateOpeningTimeCommand.builder()
                    .sportObjectId(sportObjectId)
                    .price(new Price(new BigDecimal(openingTimeDto.getPrice())))
                    .timeRange(timeRange)
                    .build());

            return openingTimeRepository.findOne(
                    OpeningTimeQueryExpressions.sportObjectIdAndTimeRangeMatches(sportObjectId, timeRange))
                    .<ResponseEntity<?>> map(openingTime -> ok(OpeningTimeDtoFactory.create(openingTime)))
                    .orElse(errorResponseService.create("id", ErrorCode.NOT_EXISTS, HttpStatus.CONFLICT));
        } else {
            return badRequest().build();
        }
    }

    @PutMapping(DIRECTOR_API_OPENING_TIME_BY_ID)
    ResponseEntity<?> updateOpeningHours(@PathVariable UUID sportsclubId,
                                         @PathVariable UUID sportObjectId,
                                         @PathVariable UUID openingTimeId,
                                         @RequestBody @Validated OpeningTimeDto openingTimeDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorResponseService.create(bindingResult);
        }

        TimeDto startTime = openingTimeDto.getStartTime();
        TimeDto finishTime = openingTimeDto.getFinishTime();

        Runnable sendCommand = () -> commandGateway.sendAndWait(UpdateOpeningTimeCommand.builder()
                .openingTimeId(openingTimeId)
                .sportObjectId(sportObjectId)
                .timeRange(new OpeningTimeRange(
                        openingTimeDto.getDayOfWeek(),
                        LocalTime.of(startTime.getHour(), startTime.getMinute()),
                        LocalTime.of(finishTime.getHour(), finishTime.getMinute())))
                .price(new Price(new BigDecimal(openingTimeDto.getPrice())))
                .build());

        boolean commandSent = sendCommand(sportsclubId, sportObjectId, openingTimeId, sendCommand);
        return commandSent ? noContent().build() : badRequest().build();
    }

    @DeleteMapping(DIRECTOR_API_OPENING_TIME_BY_ID)
    ResponseEntity<?> deleteOpeningHours(@PathVariable UUID sportsclubId,
                                         @PathVariable UUID sportObjectId,
                                         @PathVariable UUID openingTimeId) {
        Runnable sendCommand = () -> commandGateway.sendAndWait(DeleteOpeningTimeCommand.builder()
                .openingTimeId(openingTimeId)
                .sportObjectId(sportObjectId)
                .build());

        boolean commandSent = sendCommand(sportsclubId, sportObjectId, openingTimeId, sendCommand);
        return commandSent ? noContent().build() : badRequest().build();
    }

    private boolean sendCommand(UUID sportsclubId,
                                UUID sportObjectId,
                                UUID openingTimeId,
                                Runnable sendCommand) {
        boolean sportsclubExists = sportsclubRepository.exists(
                SportsclubQueryExpressions.idMatches(sportsclubId));

        boolean sportObjectExists = sportObjectRepository.exists(
                SportObjectQueryExpressions.idMatches(sportObjectId));

        boolean openingTimeExists = openingTimeRepository.exists(
                OpeningTimeQueryExpressions.idMatches(openingTimeId));

        if (sportsclubExists && sportObjectExists && openingTimeExists) {
            sendCommand.run();
            return true;
        } else {
            return false;
        }
    }

    @GetMapping(DIRECTOR_API_DAY_OPENING_TIME)
    @ResponseStatus(HttpStatus.OK)
    public List<DayOpeningTimeDto> get(@PathVariable UUID sportObjectId) {
        List<OpeningTimeEntity> openingTimes = newArrayList(openingTimeRepository.findAll(
                OpeningTimeQueryExpressions.sportObjectIdMatches(sportObjectId)));

        return getDayOpeningTimes(openingTimes);
    }

    @PostMapping(DIRECTOR_API_DAY_OPENING_TIME)
    public ResponseEntity<?> create(@PathVariable UUID sportObjectId,
                                  @RequestBody DayOpeningTimeDto dayOpeningTimeDto) {
        DayOfWeek day = DayOfWeek.valueOf(dayOpeningTimeDto.getDayOfWeek().toUpperCase());

        openingTimeRepository.findAll(OpeningTimeQueryExpressions.sportObjectIdAndDayMatches(sportObjectId, day))
                .forEach(openingTime -> commandGateway.send(new DeleteOpeningTimeCommand(sportObjectId, openingTime.getId())));

        int timeInterval = dayOpeningTimeDto.getTimeInterval();
        LocalTime finishTime = LocalTime.of(dayOpeningTimeDto.getFinishTime().getHour(), dayOpeningTimeDto.getFinishTime().getMinute());
        LocalTime currentTime = LocalTime.of(dayOpeningTimeDto.getStartTime().getHour(), dayOpeningTimeDto.getStartTime().getMinute());
        LocalTime nextTime;

        while ((nextTime = currentTime.plusMinutes(timeInterval)).compareTo(finishTime) < 0) {
            commandGateway.send(CreateOpeningTimeCommand.builder()
                    .sportObjectId(sportObjectId)
                    .timeRange(new OpeningTimeRange(day, currentTime, nextTime))
                    .price(new Price(new BigDecimal(dayOpeningTimeDto.getPrice())))
                    .build());
            currentTime = nextTime;
        }

        List<DayOpeningTimeDto> dayOpeningTimes = getDayOpeningTimes(
                newArrayList(openingTimeRepository.findAll(OpeningTimeQueryExpressions.sportObjectIdAndDayMatches(sportObjectId, day))));

        if (dayOpeningTimes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return ok(dayOpeningTimes.get(0));
        }
    }

    private List<DayOpeningTimeDto> getDayOpeningTimes(List<OpeningTimeEntity> openingTimes) {
        Comparator<OpeningTimeEntity> startTimeComparator = comparing(o -> o.getTimeRange().getStartTime());
        List<DayOpeningTimeDto> dayOpeningTimes = new ArrayList<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            Optional<OpeningTimeEntity> firstOpeningTimeForDayOptional = openingTimes.stream()
                    .filter(o -> o.getTimeRange().getDayOfWeek() == day)
                    .min(startTimeComparator);

            if (firstOpeningTimeForDayOptional.isPresent()) {
                OpeningTimeEntity firstOpeningTimeForDay = firstOpeningTimeForDayOptional.get();
                OpeningTimeEntity lastOpeningTimeForDay = openingTimes.stream()
                        .filter(o -> o.getTimeRange().getDayOfWeek() == day)
                        .max(startTimeComparator).get();

                dayOpeningTimes.add(DayOpeningTimeDtoFactory.create(day, firstOpeningTimeForDay, lastOpeningTimeForDay));
            }
        }

        return dayOpeningTimes;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OpeningTimeRangeConflictException.class)
    public List<FieldErrorDto> handleOpeningTimeRangeConflict() {
        return errorResponseService.createBody("openingTimeRange", ErrorCode.ALREADY_EXISTS);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyDeletedException.class)
    public List<FieldErrorDto> handleAlreadyDeletedConflict() {
        return errorResponseService.createBody("openingTimeRange", ErrorCode.ALREADY_DELETED);
    }
}
