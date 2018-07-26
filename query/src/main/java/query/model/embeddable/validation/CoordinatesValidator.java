package query.model.embeddable.validation;

import commons.ErrorCode;
import org.apache.commons.lang3.Range;
import org.springframework.validation.Errors;

public class CoordinatesValidator {

    private static final Range<Double> latitudeRange = Range.between(-90d, 90d);
    private static final Range<Double> longitudeRange = Range.between(-180d, 180d);

    private static final String LATITUDE_ERROR_CODE_PREFIX = "latitude";
    private static final String LONGITUDE_ERROR_CODE_PREFIX = "longitude";

    public static boolean isInvalid(Double latitude, Double longitude) {
        return !latitudeRange.contains(latitude) || !longitudeRange.contains(longitude);
    }

    public static void validate(Double latitude, Double longitude, Errors errors) {
        if (latitude == null) {
            errors.rejectValue("coordinates", ErrorCode.EMPTY.getCodeWithPrefix(LATITUDE_ERROR_CODE_PREFIX));
        } else if (!latitudeRange.contains(latitude)) {
            errors.rejectValue("coordinates", ErrorCode.OUT_OF_RANGE.getCodeWithPrefix(LATITUDE_ERROR_CODE_PREFIX));
        }

        if (longitude == null) {
            errors.rejectValue("coordinates", ErrorCode.EMPTY.getCodeWithPrefix(LONGITUDE_ERROR_CODE_PREFIX));
        } else if (!longitudeRange.contains(longitude)) {
            errors.rejectValue("coordinates", ErrorCode.OUT_OF_RANGE.getCodeWithPrefix(LONGITUDE_ERROR_CODE_PREFIX));
        }
    }
}
