package com.chex.tracer.api.managers;

import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.models.User;
import com.chex.tracer.api.services.StructureService;
import com.chex.tracer.api.services.UserService;
import com.chex.tracer.api.utils.NullOnEmptyConverterFactory;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
    }

    private RequestBody createJSONRequestBody(JSONObject params){
        return RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                params.toString()
        );
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

    public void login(String user, String pwd, final APICallBack callBack){
        try{
            JSONObject params = new JSONObject();
            params.put("user", user);
            params.put("pwd", encryptPassword(pwd));
            Call<String> callUserLogin = userService.login(createJSONRequestBody(params));
            callUserLogin.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    callBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {
                    throwable.printStackTrace();
                    callBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void signup(String username, String email, String pwd, final APICallBack callBack){
        try{
            JSONObject params = new JSONObject();
            params.put("username", username);
            params.put("email", email);
            params.put("pwd", encryptPassword(pwd));

            Call<String> callUserSignup = userService.signup(createJSONRequestBody(params));
            callUserSignup.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    callBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {
                    throwable.printStackTrace();
                    callBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
