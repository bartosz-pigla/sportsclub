package boot.security;

import static query.model.user.repository.UserQueryExpressions.usernameMatches;

import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import query.model.user.UserEntity;
import query.model.user.repository.UserEntityRepository;
import query.model.user.repository.UserQueryExpressions;
import web.publicApi.signIn.dto.UserPrincipal;

@Service
@AllArgsConstructor
class CustomUserDetailsService implements UserDetailsService {

    private UserEntityRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserEntity user = userRepository.findOne(usernameMatches(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new UserPrincipal(user);
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        UserEntity user = userRepository.findOne(
                UserQueryExpressions.idMatches(UUID.fromString(id)))
                .orElseThrow(RuntimeException::new);
        return new UserPrincipal(user);
    }
}
