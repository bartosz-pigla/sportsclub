package web.user;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;
import static query.model.user.QUserEntity.userEntity;
import static query.model.user.repository.UserQueryExpressions.idMatches;
import static web.common.RequestMappings.RECEPTIONIST_API_USER;
import static web.common.RequestMappings.RECEPTIONIST_API_USER_BY_ID;

import java.util.UUID;

import com.querydsl.core.types.Predicate;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserEntity;
import web.user.dto.UserDto;
import web.user.dto.UserDtoFactory;

@RestController
@Setter(onMethod_ = { @Autowired })
public class UserReceptionistController extends UserBaseController {

    @GetMapping(RECEPTIONIST_API_USER)
    public Page<UserDto> get(
            @QuerydslPredicate(root = UserEntity.class) Predicate predicate,
            @PageableDefault(sort = "username", direction = Sort.Direction.ASC) Pageable pageable) {
        return this.userRepository
                .findAll(isNotDeleted(userEntity._super).and(predicate), pageable)
                .map(UserDtoFactory::create);
    }

    @GetMapping(RECEPTIONIST_API_USER_BY_ID)
    public ResponseEntity<?> get(@PathVariable UUID userId) {
        return userRepository.findOne(idMatches(userId))
                .map(user -> ok(UserDtoFactory.create(user)))
                .orElse(notFound().build());
    }
}
