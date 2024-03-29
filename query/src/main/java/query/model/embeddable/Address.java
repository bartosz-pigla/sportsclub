package query.model.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Address {

    @Column
    private String street;
    @Embedded
    private City city;
    @Embedded
    private Coordinates coordinates;
}
