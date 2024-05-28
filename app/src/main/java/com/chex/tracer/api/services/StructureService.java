package com.chex.tracer.api.services;

public abstract class StructureService {
    public static final String BASE_URL = "https://tracer-apirest.vercel.app/";

    //COMMONS
    private static final String DELETE = "delete/{id}/";

    //USER
    public static final String BASE_USER = "user/";
    public static final String GET_USER_BY_ID = BASE_USER + "{id}/";
    public static final String IS_USERNAME_AVAILABLE = BASE_USER + "isusernameavailable/{username}/";
    public static final String IS_EMAIL_AVAILABLE = BASE_USER + "isemailavailable/{email}/";
    public static final String LOGIN = BASE_USER + "login/";
    public static final String SIGNUP = BASE_USER + "signup/";
    public static final String SOCIAL_MEDIA_DATA = BASE_USER + "{id}/socialmediadata/";
    public static final String USER_GAME_DATA = BASE_USER + "{userId}/gamedetails/{gameId}/";
    public static final String UPDATE_GAME_DATA = BASE_USER + "updategamedata/";
    public static final String IS_FOLLOWING = BASE_USER + "isFollowing/";
    public static final String EDIT_USER = BASE_USER + "edit/";
    public static final String DELETE_USER = BASE_USER + DELETE;

    //VIDEOGAME
    public static final String BASE_VG = "videogame/";
    public static final String GET_LATEST_VG = BASE_VG + "latest/";
    public static final String GET_VG_BY_ID = BASE_VG + "{id}/";
    public static final String GET_VG_STORES = GET_VG_BY_ID + "stores/";
    public static final String GET_VG_PLATFORMS = GET_VG_BY_ID + "platforms/";
    public static final String GET_VG_RATINGS = GET_VG_BY_ID + "ratings/";

    //REVIEW
    public static final String BASE_REVIEW = "review/";
    public static final String GET_REVIEW = BASE_REVIEW + "{gameId}/{userId}/";
    public static final String UPDATE_REVIEW = BASE_REVIEW + "edit/";
}
