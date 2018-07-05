package query.model.embeddable;

import static query.validation.PriceValidator.isInvalid;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import query.exception.ValueObjectCreationException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Price {

    private static final int PRICE_DECIMAL_PLACES = 2;

    @Column
    private BigDecimal price;

    public Price(BigDecimal price) {
        if (isInvalid(price)) {
            throw new ValueObjectCreationException();
        } else {
            this.price = price.setScale(PRICE_DECIMAL_PLACES, BigDecimal.ROUND_CEILING);
        }
    }
}
