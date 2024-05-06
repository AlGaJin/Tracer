package com.chex.tracer.api.services;

public abstract class StructureService {
    public static final String BASE_URL = "http://localhost:8000/";

    //COMMONS
    private static final String DELETE = "delete/{id}/";

    //USER
    public static final String BASE_USER = "user/";
    public static final String GET_USER_BY_ID = BASE_USER + "{id}";
    public static final String LOGIN = BASE_USER + "login/";
    public static final String SIGNUP = BASE_USER + "signup/";
    public static final String DELETE_USER = BASE_USER + DELETE;

}
