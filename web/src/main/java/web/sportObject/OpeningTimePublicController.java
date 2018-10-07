package web.sportObject;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static query.model.sportobject.repository.OpeningTimeQueryExpressions.sportObjectIdMatches;
import static web.common.RequestMappings.PUBLIC_API_OPENING_TIME;

import java.util.List;
import java.util.UUID;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import query.model.sportobject.repository.OpeningTimeEntityRepository;
import web.common.BaseController;
import web.sportObject.dto.OpeningTimeDto;
import web.sportObject.dto.OpeningTimeDtoFactory;

@RestController
@Setter(onMethod_ = { @Autowired })
final class OpeningTimePublicController extends BaseController {

    private OpeningTimeEntityRepository openingTimeRepository;

    @GetMapping(PUBLIC_API_OPENING_TIME)
    List<OpeningTimeDto> get(@PathVariable UUID sportObjectId) {
        return newArrayList(openingTimeRepository
                .findAll(sportObjectIdMatches(sportObjectId), Sort.by("timeRange.dayOfWeek", "timeRange.startTime")))
                .stream()
                .map(OpeningTimeDtoFactory::create)
                .collect(toList());
    }
}
