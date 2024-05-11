package com.chex.tracer.api.services;

import com.chex.tracer.api.models.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    //Llamadas GET
    @GET(StructureService.BASE_URL)
    Call<List<User>> getAllUsers();
    @GET(StructureService.GET_USER_BY_ID)
    Call<User> getUserByID(@Path("id") int userId);

    //Llamadas POST
    @POST(StructureService.LOGIN)
    Call<String> login(@Body RequestBody params);
    @POST(StructureService.SIGNUP)
    Call<String> signup(@Body RequestBody params);


    //Llamadas a DELETE
    @DELETE(StructureService.DELETE_USER)
    Call<Boolean> deleteUser(@Path("id") int userId);
}
