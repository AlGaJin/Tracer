package com.chex.tracer.api.managers;

import android.util.Log;

import com.chex.tracer.api.models.User;
import com.chex.tracer.api.services.StructureService;
import com.chex.tracer.api.services.UserService;

import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserManager {
    private final Retrofit retrofit;
    private final UserService userService;

    public UserManager(){
        retrofit = new Retrofit.Builder()
                .baseUrl(StructureService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
    }

    public User login(String user, String pwd){
        try{
            JSONObject params = new JSONObject();
            params.put("user", user);
            params.put("pwd", pwd);
            Call<User> userFromApi = userService.login(params);
            final User[] userLoged = {new User()};
            userFromApi.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    userLoged[0] = response.body();
                }

                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
            return userLoged[0];
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
