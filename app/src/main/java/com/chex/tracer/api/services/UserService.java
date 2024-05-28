package com.chex.tracer.api.services;

import com.chex.tracer.api.models.Store;
import com.chex.tracer.api.models.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {
    //Llamadas GET
    @GET(StructureService.BASE_USER)
    Call<List<User>> getAllUsers();
    @GET(StructureService.GET_USER_BY_ID)
    Call<User> getUserByID(@Path("id") int userId);
    @GET(StructureService.IS_USERNAME_AVAILABLE)
    Call<Boolean> isUsernameAvailable(@Path("username") String username);
    @GET(StructureService.IS_EMAIL_AVAILABLE)
    Call<Boolean> isEmailAvailable(@Path("email") String email);
    @GET(StructureService.SOCIAL_MEDIA_DATA)
    Call<List<String>> getSocialMediaDataFrom(@Path("id") int userId);
    @GET(StructureService.USER_GAME_DATA)
    Call<List<Boolean>> getGameDataFrom(@Path("userId") int userId, @Path("gameId") int gameId);

    //Llamadas POST
    @POST(StructureService.LOGIN)
    Call<String> login(@Body RequestBody params);
    @POST(StructureService.SIGNUP)
    Call<String> signup(@Body RequestBody params);
    @POST(StructureService.IS_FOLLOWING)
    Call<Boolean> isFollowing(@Body RequestBody params);

    //Llamadas PUT
    @PUT(StructureService.EDIT_USER)
    Call<Boolean> editUser(@Body RequestBody params);
    @PUT(StructureService.UPDATE_GAME_DATA)
    Call<Void> updateGameData(@Body RequestBody params);


    //Llamadas a DELETE
    @DELETE(StructureService.DELETE_USER)
    Call<Boolean> deleteUser(@Path("id") int userId);
}
