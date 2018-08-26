package query.model.user;

import static java.util.Arrays.stream;

import java.util.EnumSet;
import java.util.stream.Stream;

public enum UserType {

    CUSTOMER,
    DIRECTOR,
    RECEPTIONIST;

    public static final EnumSet<UserType> ALL = EnumSet.allOf(UserType.class);

    public static String[] getNames(UserType... userTypes) {
        return getNames(stream(userTypes));
    }

    public static String[] getNames(EnumSet<UserType> userTypes) {
        return getNames(userTypes.stream());
    }

    public static String[] getNames(Stream<UserType> userTypeStream) {
        return userTypeStream.map(Enum::name).toArray(String[]::new);
    }
}
