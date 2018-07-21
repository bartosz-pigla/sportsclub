package boot.security;

import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import query.model.user.UserEntity;
import query.repository.UserEntityRepository;
import web.signIn.dto.UserPrincipal;

@Service
@AllArgsConstructor
class CustomUserDetailsService implements UserDetailsService {

    private UserEntityRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new UserPrincipal(user);
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        UserEntity user = userRepository.findById(UUID.fromString(id)).orElseThrow(RuntimeException::new);
        return new UserPrincipal(user);
    }
}
