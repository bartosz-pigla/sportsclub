package query.model.user;

import java.util.Arrays;

public enum UserType {
    CUSTOMER,
    DIRECTOR,
    RECEPTIONIST;

    public static String[] getNames(UserType... userTypes) {
        return Arrays.stream(userTypes).map(Enum::name).toArray(String[]::new);
    }
}
