package web.common;

import static web.common.RequestParameters.ANNOUNCEMENT_ID_PARAMETER;
import static web.common.RequestParameters.BOOKING_DETAIL_ID_PARAMETER;
import static web.common.RequestParameters.BOOKING_ID_PARAMETER;
import static web.common.RequestParameters.OPENING_TIME_ID_PARAMETER;
import static web.common.RequestParameters.SPORTSCLUB_NAME_PARAMETER;
import static web.common.RequestParameters.SPORT_OBJECT_NAME_PARAMETER;
import static web.common.RequestParameters.SPORT_OBJECT_POSITION_NAME_PARAMETER;
import static web.common.RequestParameters.USERNAME_PARAMETER;

public final class RequestMappings {

    public static final String API = "/api";

    //AUTHENTICATION FOR EVERYONE

    public static final String AUTH = API + "/auth";
    public static final String SIGN_IN = AUTH + "/sign-in";
    public static final String SIGN_UP = AUTH + "/sign-up";
    public static final String CUSTOMER_ACTIVATION = AUTH + "/customer-activation";

    //ADMIN API

    private static final String ADMIN_API = API + "/admin-api";

    public static final String ADMIN_API_USER_ACTIVATION = ADMIN_API + "/user-activation";

    public static final String ADMIN_API_CUSTOMER = ADMIN_API + "/customer";
    public static final String ADMIN_API_CUSTOMER_BY_USERNAME = ADMIN_API_CUSTOMER + USERNAME_PARAMETER;

    public static final String ADMIN_API_DIRECTOR = ADMIN_API + "/director";
    public static final String ADMIN_API_DIRECTOR_BY_USERNAME = ADMIN_API_DIRECTOR + USERNAME_PARAMETER;

    public static final String ADMIN_API_RECEPTIONIST = ADMIN_API + "/receptionist";
    public static final String ADMIN_API_RECEPTIONIST_BY_USERNAME = ADMIN_API_RECEPTIONIST + USERNAME_PARAMETER;

    public static final String ADMIN_API_SPORTSCLUB = ADMIN_API + "/sportsclub";
    public static final String ADMIN_API_SPORTSCLUB_BY_NAME = ADMIN_API_SPORTSCLUB + SPORTSCLUB_NAME_PARAMETER;

    public static final String ADMIN_API_STATUTE = ADMIN_API_SPORTSCLUB_BY_NAME + "/statute";

    public static final String ADMIN_API_ANNOUNCEMENT = ADMIN_API_SPORTSCLUB_BY_NAME + "/announcement";
    public static final String ADMIN_API_SPORTSCLUB_ANNOUNCEMENT_BY_ID = ADMIN_API_ANNOUNCEMENT + ANNOUNCEMENT_ID_PARAMETER;

    public static final String ADMIN_API_SPORT_OBJECT = ADMIN_API_SPORTSCLUB_BY_NAME + "/sport-object";
    public static final String ADMIN_API_SPORT_OBJECT_BY_NAME = ADMIN_API_SPORT_OBJECT + SPORT_OBJECT_NAME_PARAMETER;
    public static final String ADMIN_API_OPENING_TIME = ADMIN_API_SPORT_OBJECT_BY_NAME + "/opening-time";
    public static final String ADMIN_API_OPENING_TIME_BY_ID = ADMIN_API_OPENING_TIME + OPENING_TIME_ID_PARAMETER;
    public static final String ADMIN_API_SPORT_OBJECT_POSITION = ADMIN_API_SPORT_OBJECT_BY_NAME + "sport-object-position";
    public static final String ADMIN_API_SPORT_OBJECT_POSITION_BY_NAME = ADMIN_API_SPORT_OBJECT_POSITION + SPORT_OBJECT_POSITION_NAME_PARAMETER;

    //CUSTOMER API

    public static final String CUSTOMER_API = API + "/customer-api";
    public static final String CUSTOMER_API_BOOKING = CUSTOMER_API + "/booking";
    public static final String CUSTOMER_API_BOOKING_BY_ID = CUSTOMER_API_BOOKING + BOOKING_ID_PARAMETER;
    public static final String CUSTOMER_API_BOOKING_DETAIL = CUSTOMER_API_BOOKING + "/detail";
    public static final String CUSTOMER_API_BOOKING_DETAIL_BY_ID = CUSTOMER_API_BOOKING_DETAIL + BOOKING_DETAIL_ID_PARAMETER;

    public static String getAntMatcher(String requestMapping) {
        return requestMapping + "/**";
    }
}
