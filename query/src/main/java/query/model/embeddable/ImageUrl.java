package query.model.embeddable;

import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import query.exception.ValueObjectCreationException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUrl {

    @Column
    private URL imageUrl;

    public ImageUrl(String url) {
        try {
            imageUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new ValueObjectCreationException();
        }
    }
}
