package web.common;

import static web.common.RequestParameters.USERNAME_PARAMETER;

public final class RequestMappings {

    public static final String API = "/api";

    //AUTHENTICATION FOR EVERYONE
    public static final String AUTH = API + "/auth";
    public static final String SIGN_IN = AUTH + "/sign-in";
    public static final String SIGN_UP = AUTH + "/sign-up";
    public static final String CUSTOMER_ACTIVATION = AUTH + "/customer-activation";

    //ADMIN CONSOLE
    private static final String ADMIN_CONSOLE = API + "/adminConsole";

    public static final String ADMIN_CONSOLE_USER_ACTIVATION = ADMIN_CONSOLE + "/user-activation";

    public static final String ADMIN_CONSOLE_CUSTOMER = ADMIN_CONSOLE + "/customer";
    public static final String ADMIN_CONSOLE_CUSTOMER_BY_USERNAME = ADMIN_CONSOLE_CUSTOMER + USERNAME_PARAMETER;

    public static final String ADMIN_CONSOLE_DIRECTOR = ADMIN_CONSOLE + "/director";
    public static final String ADMIN_CONSOLE_DIRECTOR_BY_USERNAME = ADMIN_CONSOLE_DIRECTOR + USERNAME_PARAMETER;

    public static final String ADMIN_CONSOLE_RECEPTIONIST = ADMIN_CONSOLE + "/receptionist";
    public static final String ADMIN_CONSOLE_RECEPTIONIST_BY_USERNAME = ADMIN_CONSOLE_RECEPTIONIST + USERNAME_PARAMETER;

    public static final String ADMIN_CONSOLE_STATUTE = ADMIN_CONSOLE + "statute";

    public static String getAntMatcher(String requestMapping) {
        return requestMapping + "/**";
    }

    public static String getParameteredRequestMapping(String requestMapping, String parameterKey, String parameterValue) {
        return requestMapping.replace(parameterKey, "/" + parameterValue);
    }
}
