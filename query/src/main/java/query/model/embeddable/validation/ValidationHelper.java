package query.model.embeddable.validation;

class ValidationHelper {

    static boolean hasInvalidLength(String str, int minLength, int maxLength) {
        return str.length() < minLength || str.length() > maxLength;
    }
}
