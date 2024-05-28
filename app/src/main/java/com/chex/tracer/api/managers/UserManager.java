package com.chex.tracer.api.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.models.User;
import com.chex.tracer.api.services.StructureService;
import com.chex.tracer.api.services.UserService;
import com.chex.tracer.api.utils.NullOnEmptyConverterFactory;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserManager extends BaseManager {
    private final UserService userService;

    public UserManager(){
        super();
        userService = retrofit.create(UserService.class);
    }

    private static String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getUser(int id, final APICallBack apiCallBack){
        userService.getUserByID(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                apiCallBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                throwable.printStackTrace();
                apiCallBack.onError();
            }
        });
    }

    public void isUsernameAvailable(String username, final APICallBack apiCallBack){
        userService.isUsernameAvailable(username).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                apiCallBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                throwable.printStackTrace();
                apiCallBack.onError();
            }
        });
    }

    public void isEmailAvailable(String email, final APICallBack apiCallBack){
        userService.isEmailAvailable(email).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                apiCallBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                throwable.printStackTrace();
                apiCallBack.onError();
            }
        });
    }

    public void getUserGameData(int userId, int gameId, final APICallBack apiCallBack){
        userService.getGameDataFrom(userId, gameId).enqueue(new Callback<List<Boolean>>() {
            @Override
            public void onResponse(Call<List<Boolean>> call, Response<List<Boolean>> response) {
                apiCallBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Boolean>> call, Throwable throwable) {
                throwable.printStackTrace();
                apiCallBack.onError();
            }
        });
    }

    public void login(String user, String pwd, final APICallBack apiCallBack){
        try{
            JSONObject params = new JSONObject();
            params.put("user", user);
            params.put("pwd", encryptPassword(pwd));
            userService.login(createJSONRequestBody(params)).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    apiCallBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {
                    throwable.printStackTrace();
                    apiCallBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void signup(String username, String email, String pwd, final APICallBack apiCallBack){
        try{
            JSONObject params = new JSONObject();
            params.put("username", username);
            params.put("email", email);
            params.put("pwd", encryptPassword(pwd));

            userService.signup(createJSONRequestBody(params)).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    apiCallBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {
                    throwable.printStackTrace();
                    apiCallBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getSocialMediaData(int userId, final APICallBack apiCallBack){
        userService.getSocialMediaDataFrom(userId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                apiCallBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable throwable) {
                throwable.printStackTrace();
                apiCallBack.onError();
            }
        });
    }

    public void updateUser(int userId, String newUsername, String newEmail, String descr, String newProfilePic,final APICallBack apiCallBack){
        try{
            JSONObject params = new JSONObject();
            params.put("id", userId);
            if(!newUsername.isEmpty()) params.put("username", newUsername);
            if(!newEmail.isEmpty()) params.put("email", newEmail);
            params.put("descr", descr);
            params.put("profile_pic", newProfilePic);
            userService.editUser(createJSONRequestBody(params)).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    apiCallBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable throwable) {
                    throwable.printStackTrace();
                    apiCallBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateGameData(int userId, int gameId, Boolean played, Boolean playing, Boolean backlog, Float rate){
        try{
            JSONObject params = new JSONObject();
            params.put("userId", userId);
            params.put("gameId", gameId);
            params.put("played", played);
            params.put("playing", playing);
            params.put("backlog", backlog);
            params.put("rate", rate);

            userService.updateGameData(createJSONRequestBody(params)).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {}
                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void isFollowing(int followerId, int followedId, final APICallBack apiCallBack){
        try{
            JSONObject params = new JSONObject();
            params.put("followerId", followerId);
            params.put("followedId", followedId);

            userService.isFollowing(createJSONRequestBody(params)).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.body() != null){
                       apiCallBack.onSuccess(response.body());
                    }else{
                        apiCallBack.onSuccess(false);
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable throwable) {
                    throwable.printStackTrace();
                    apiCallBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
