package com.chex.tracer.api.services;

import com.chex.tracer.api.models.User;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    //Llamadas GET
    @GET(StructureService.BASE_URL)
    Call<ArrayList<User>> getAllUsers();
    @GET(StructureService.GET_USER_BY_ID)
    Call<User> getUserByID(@Path("id") int userId);

    //Llamadas POST
    @POST(StructureService.LOGIN)
    Call<User> login(@Body JSONObject params);
    @POST(StructureService.SIGNUP)
    Call<Integer> signup(@Body JSONObject params);

    //Llamadas a DELETE
    @DELETE(StructureService.DELETE_USER)
    Call<Boolean> deleteUser(@Path("id") int userId);
}
