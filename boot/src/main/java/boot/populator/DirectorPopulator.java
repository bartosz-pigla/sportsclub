package boot.populator;

import javax.annotation.PostConstruct;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserEntity;
import query.model.user.UserType;
import query.repository.UserEntityRepository;

@Service
@AllArgsConstructor
final class DirectorPopulator {

    private UserEntityRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initializeDirector() {
        UserEntity director = new UserEntity();
        director.setUserType(UserType.DIRECTOR);
        director.setEmail(new Email("bartek217a@wp.pl"));
        director.setPhoneNumber(new PhoneNumber("+48664220607"));
        director.setUsername("superuser");
        director.setPassword(passwordEncoder.encode("password"));
        director.setActivated(true);
        userRepository.save(director);
    }
}
