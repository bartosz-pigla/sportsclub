package web.sportObject;

import static com.google.common.collect.Lists.newArrayList;
import static web.common.RequestMappings.PUBLIC_API_OPENING_TIME;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.sportobject.OpeningTimeEntity;
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import query.model.sportobject.repository.OpeningTimeQueryExpressions;
import web.common.BaseController;
import web.sportObject.dto.OpeningTimeDto;
import web.sportObject.dto.OpeningTimeDtoFactory;

@RestController
@Setter(onMethod_ = { @Autowired })
final class OpeningTimePublicController extends BaseController {

    private OpeningTimeEntityRepository openingTimeRepository;

    @GetMapping(PUBLIC_API_OPENING_TIME)
    @ResponseStatus(HttpStatus.OK)
    List<OpeningTimeDto> get(@PathVariable UUID sportObjectId) {
        List<OpeningTimeEntity> openingTimes = newArrayList(openingTimeRepository
                .findAll(OpeningTimeQueryExpressions.sportObjectIdMatches(sportObjectId)));

        return openingTimes.stream().map(OpeningTimeDtoFactory::create).collect(Collectors.toList());
    }
}
