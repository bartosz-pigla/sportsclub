package query.model.embeddable;

import static query.model.embeddable.validation.CityValidator.isInvalid;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import query.exception.ValueObjectCreationException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class City {

    @Column
    private String city;

    public City(String city) {
        if (isInvalid(city)) {
            throw new ValueObjectCreationException();
        } else {
            this.city = city;
        }
    }
}
