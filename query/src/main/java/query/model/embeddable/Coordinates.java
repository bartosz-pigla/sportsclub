package query.model.embeddable;

import static query.validation.CoordinatesValidator.isInvalid;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import query.exception.ValueObjectCreationException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Coordinates {

    @Column
    private Double latitude, longitude;

    public Coordinates(Double latitude, Double longitude) {
        if (isInvalid(latitude, longitude)) {
            throw new ValueObjectCreationException();
        } else {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
