package query.model.embeddable.validation;

import org.apache.commons.lang3.Range;
import org.springframework.validation.Errors;

public class CoordinatesValidator {

    private static final Range<Double> latitudeRange = Range.between(-90d, 90d);
    private static final Range<Double> longitudeRange = Range.between(-180d, 180d);

    public static boolean isInvalid(Double latitude, Double longitude) {
        return !latitudeRange.contains(latitude) || !longitudeRange.contains(longitude);
    }

    public static void validate(Double latitude, Double longitude, Errors errors) {
        if (latitude == null) {
            errors.rejectValue("coordinates", "coordinates.latitude.empty");
        } else if (!latitudeRange.contains(latitude)) {
            errors.rejectValue("coordinates", "coordinates.latitude.outOfRange");
        }

        if (longitude == null) {
            errors.rejectValue("coordinates", "coordinates.longitude.empty");
        } else if (!longitudeRange.contains(longitude)) {
            errors.rejectValue("coordinates", "coordinates.longitude.outOfRange");
        }
    }
}
