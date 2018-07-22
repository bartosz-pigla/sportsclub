package web.common;

public class RequestMappings {

    public static final String API = "/api";

    //AUTHENTICATION FOR EVERYONE
    public static final String AUTH = API + "/auth";
    public static final String SIGN_IN = AUTH + "/sign-in";

    //CUSTOMER AUTHENTICATION
    public static final String SIGN_UP = AUTH + "/sign-up";
    public static final String CUSTOMER_ACTIVATION = "/customer-activation";

    //STAFF MANAGEMENT
    public static final String DIRECTOR = API + "/director";
    public static final String RECEPTIONIST = API + "/receptionist";

    public static String getAntMatcher(String requestMapping) {
        return requestMapping + "/**";
    }
}
