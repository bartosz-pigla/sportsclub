package web.common;

import static web.common.RequestParameters.ANNOUNCEMENT_ID_PARAMETER;
import static web.common.RequestParameters.BOOKING_DETAIL_ID_PARAMETER;
import static web.common.RequestParameters.BOOKING_ID_PARAMETER;
import static web.common.RequestParameters.OPENING_TIME_ID_PARAMETER;
import static web.common.RequestParameters.SPORTSCLUB_ID_PARAMETER;
import static web.common.RequestParameters.SPORT_OBJECT_ID_PARAMETER;
import static web.common.RequestParameters.SPORT_OBJECT_POSITION_ID_PARAMETER;
import static web.common.RequestParameters.USER_ID_PARAMETER;

public final class RequestMappings {

    //PUBLIC API

    public static final String PUBLIC_API = "/public-api";

    public static final String PUBLIC_API_SIGN_IN = PUBLIC_API + "/sign-in";
    public static final String PUBLIC_API_SIGN_UP = PUBLIC_API + "/sign-up";

    public static final String PUBLIC_API_CUSTOMER = PUBLIC_API + "/customer";
    public static final String PUBLIC_API_CUSTOMER_ACTIVATE = PUBLIC_API_CUSTOMER + USER_ID_PARAMETER + "/activate";
    public static final String PUBLIC_API_SPORTSCLUB_BY_ID = PUBLIC_API + "/sportsclub" + SPORTSCLUB_ID_PARAMETER;
    public static final String PUBLIC_API_ANNOUNCEMENT = PUBLIC_API_SPORTSCLUB_BY_ID + "/announcement";
    public static final String PUBLIC_API_SPORT_OBJECT = PUBLIC_API_SPORTSCLUB_BY_ID + "/sport-object";
    public static final String PUBLIC_API_SPORT_OBJECT_BY_ID = PUBLIC_API_SPORTSCLUB_BY_ID + "/sport-object" + SPORT_OBJECT_ID_PARAMETER;
    public static final String PUBLIC_API_SPORT_OBJECT_POSITION = PUBLIC_API_SPORT_OBJECT_BY_ID + "/sport-object-position";
    public static final String PUBLIC_API_OPENING_TIME = PUBLIC_API_SPORT_OBJECT_BY_ID + "/opening-time" + OPENING_TIME_ID_PARAMETER;

    //DIRECTOR API

    public static final String DIRECTOR_API = "/director-api";

    public static final String DIRECTOR_API_USER = DIRECTOR_API + "/user";
    public static final String DIRECTOR_API_USER_ACTIVATE = DIRECTOR_API_USER + USER_ID_PARAMETER + "/activate";

    public static final String DIRECTOR_API_CUSTOMER = DIRECTOR_API + "/customer";
    public static final String DIRECTOR_API_CUSTOMER_BY_ID = DIRECTOR_API_CUSTOMER + USER_ID_PARAMETER;

    public static final String DIRECTOR_API_DIRECTOR = DIRECTOR_API + "/director";
    public static final String DIRECTOR_API_DIRECTOR_BY_ID = DIRECTOR_API_DIRECTOR + USER_ID_PARAMETER;

    public static final String DIRECTOR_API_RECEPTIONIST = DIRECTOR_API + "/receptionist";
    public static final String DIRECTOR_API_RECEPTIONIST_BY_ID = DIRECTOR_API_RECEPTIONIST + USER_ID_PARAMETER;

    public static final String DIRECTOR_API_SPORTSCLUB = DIRECTOR_API + "/sportsclub";
    public static final String DIRECTOR_API_SPORTSCLUB_BY_ID = DIRECTOR_API_SPORTSCLUB + SPORTSCLUB_ID_PARAMETER;

    public static final String DIRECTOR_API_STATUTE = DIRECTOR_API_SPORTSCLUB_BY_ID + "/statute";

    public static final String DIRECTOR_API_ANNOUNCEMENT = DIRECTOR_API_SPORTSCLUB_BY_ID + "/announcement";
    public static final String DIRECTOR_API_SPORTSCLUB_ANNOUNCEMENT_BY_ID = DIRECTOR_API_ANNOUNCEMENT + ANNOUNCEMENT_ID_PARAMETER;

    public static final String DIRECTOR_API_SPORT_OBJECT = DIRECTOR_API_SPORTSCLUB_BY_ID + "/sport-object";
    public static final String DIRECTOR_API_SPORT_OBJECT_BY_ID = DIRECTOR_API_SPORT_OBJECT + SPORT_OBJECT_ID_PARAMETER;
    public static final String DIRECTOR_API_OPENING_TIME = DIRECTOR_API_SPORT_OBJECT_BY_ID + "/opening-time";
    public static final String DIRECTOR_API_OPENING_TIME_BY_ID = DIRECTOR_API_OPENING_TIME + OPENING_TIME_ID_PARAMETER;
    public static final String DIRECTOR_API_DAY_OPENING_TIME = DIRECTOR_API_SPORT_OBJECT_BY_ID + "/day-opening-time";
    public static final String DIRECTOR_API_SPORT_OBJECT_POSITION = DIRECTOR_API_SPORT_OBJECT_BY_ID + "/sport-object-position";
    public static final String DIRECTOR_API_SPORT_OBJECT_POSITION_BY_ID = DIRECTOR_API_SPORT_OBJECT_POSITION + SPORT_OBJECT_POSITION_ID_PARAMETER;

    //RECEPTIONIST API

    public static final String RECEPTIONIST_API = "/receptionist-api";

    public static final String RECEPTIONIST_API_USER = RECEPTIONIST_API + "/user";
    public static final String RECEPTIONIST_API_USER_BY_ID = RECEPTIONIST_API_USER + USER_ID_PARAMETER;

    public static final String RECEPTIONIST_API_BOOKING = RECEPTIONIST_API + "/booking";
    public static final String RECEPTIONIST_API_BOOKING_BY_ID = RECEPTIONIST_API_BOOKING + BOOKING_ID_PARAMETER;
    public static final String RECEPTIONIST_API_BOOKING_REJECT = RECEPTIONIST_API_BOOKING_BY_ID + "/reject";
    public static final String RECEPTIONIST_API_BOOKING_CONFIRM = RECEPTIONIST_API_BOOKING_BY_ID + "/confirm";
    public static final String RECEPTIONIST_API_BOOKING_FINISH = RECEPTIONIST_API_BOOKING_BY_ID + "/finish";

    public static final String RECEPTIONIST_API_BOOKING_DETAIL = RECEPTIONIST_API_BOOKING_BY_ID + "/detail";
    public static final String RECEPTIONIST_API_BOOKING_DETAIL_BY_ID = RECEPTIONIST_API_BOOKING_DETAIL + BOOKING_DETAIL_ID_PARAMETER;

    //CUSTOMER API

    public static final String CUSTOMER_API = "/customer-api";
    public static final String CUSTOMER_API_BOOKING = CUSTOMER_API + "/booking";
    public static final String CUSTOMER_API_BOOKING_BY_ID = CUSTOMER_API_BOOKING + BOOKING_ID_PARAMETER;
    public static final String CUSTOMER_API_BOOKING_SUBMIT = CUSTOMER_API_BOOKING_BY_ID + "/submit";
    public static final String CUSTOMER_API_BOOKING_CANCEL = CUSTOMER_API_BOOKING_BY_ID + "/cancel";
    public static final String CUSTOMER_API_BOOKING_DETAIL = CUSTOMER_API_BOOKING_BY_ID + "/detail";
    public static final String CUSTOMER_API_BOOKING_DETAIL_BY_ID = CUSTOMER_API_BOOKING_DETAIL + BOOKING_DETAIL_ID_PARAMETER;

    public static String getAntMatcher(String requestMapping) {
        return requestMapping + "/**";
    }
}
