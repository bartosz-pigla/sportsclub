package boot;

import javax.annotation.PostConstruct;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserEntity;
import query.model.user.UserType;
import query.repository.UserEntityRepository;

@Configuration
@AllArgsConstructor
public class UserEntityPopulator {

    private UserEntityRepository userEntityRepository;

    @PostConstruct
    public void foo() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserType(UserType.CUSTOMER);
        userEntity.setEmail(new Email("bartek217a@wp.pl"));
        userEntity.setPhoneNumber(new PhoneNumber("+48664220607"));
        userEntity.setUsername("customer1");
        userEntity.setPassword("password1");
        userEntityRepository.save(userEntity);

        userEntityRepository.findAll().forEach(u -> System.out.println(u.getUsername()));
    }
}
