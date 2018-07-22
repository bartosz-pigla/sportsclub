package boot;

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
class UserEntityPopulator {

    private UserEntityRepository userEntityRepository;
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void foo() {
        initializeSuperuser();
        userEntityRepository.findAll().forEach(u -> System.out.println(u.getUsername()));
    }

    public void initializeSuperuser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserType(UserType.DIRECTOR);
        userEntity.setEmail(new Email("bartek217a@wp.pl"));
        userEntity.setPhoneNumber(new PhoneNumber("+48664220607"));
        userEntity.setUsername("superuser");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userEntity.setActivated(true);
        userEntityRepository.save(userEntity);
    }
}
