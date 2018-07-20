package web.common;

public class RequestMappings {

    public static final String API = "/api";

    //AUTHENTICATION
    public static final String AUTH = API + "/auth";
    public static final String SIGN_IN = AUTH + "/sign-in";
    public static final String SIGN_UP = AUTH + "/sign-up";

    public static final String CUSTOMER_ACTIVATION = API + "/customer-activation";

    public static String getAntMatcher(String requestMapping) {
        return requestMapping + "/**";
    }
}
