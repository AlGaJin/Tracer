package com.chex.tracer.api.managers;

import android.util.Log;

import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.models.Platform;
import com.chex.tracer.api.models.Store;
import com.chex.tracer.api.models.Videogame;
import com.chex.tracer.api.services.VideogameService;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideogameManager extends BaseManager {
    private VideogameService vgService;

    public VideogameManager(){
        super();
        vgService = retrofit.create(VideogameService.class);
    }

    public void getAll(final APICallBack apiCallBack){
        try {
            vgService.getAllVideogames().enqueue(new Callback<List<Videogame>>() {
                @Override
                public void onResponse(Call<List<Videogame>> call, Response<List<Videogame>> response) {
                    apiCallBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<List<Videogame>> call, Throwable throwable) {
                    throwable.printStackTrace();
                    apiCallBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getLatest(final APICallBack apiCallBack){
        try {
            vgService.getLatestVideogames().enqueue(new Callback<List<Videogame>>() {
                @Override
                public void onResponse(Call<List<Videogame>> call, Response<List<Videogame>> response) {
                    apiCallBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<List<Videogame>> call, Throwable throwable) {
                    throwable.printStackTrace();
                    apiCallBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getById(int id, final APICallBack apiCallBack){
        try {
            vgService.getVideogameById(id).enqueue(new Callback<List<Videogame>>() {
                @Override
                public void onResponse(Call<List<Videogame>> call, Response<List<Videogame>> response) {
                    apiCallBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<List<Videogame>> call, Throwable throwable) {
                    throwable.printStackTrace();
                    apiCallBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getStores(int id, final APICallBack apiCallBack){
        try {
            vgService.getVideogameStores(id).enqueue(new Callback<List<Store>>() {
                @Override
                public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                    apiCallBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<List<Store>> call, Throwable throwable) {
                    throwable.printStackTrace();
                    apiCallBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getPlatforms(int id, final APICallBack apiCallBack){
        try {
            vgService.getVideogamesPlatforms(id).enqueue(new Callback<List<Platform>>() {
                @Override
                public void onResponse(Call<List<Platform>> call, Response<List<Platform>> response) {
                    apiCallBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<List<Platform>> call, Throwable throwable) {
                    throwable.printStackTrace();
                    apiCallBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getUserRate(int gameId, int userId, final APICallBack apiCallBack){
        try{
            JSONObject params = new JSONObject();
            params.put("gameId", gameId);
            params.put("userId", userId);

            vgService.getUserGameRate(gameId, userId).enqueue(new Callback<Float>() {
                @Override
                public void onResponse(Call<Float> call, Response<Float> response) {
                    apiCallBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<Float> call, Throwable throwable) {
                    throwable.printStackTrace();
                    apiCallBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getRatings(int id, final APICallBack apiCallBack){
        vgService.getVideogamesRatings(id).enqueue(new Callback<List<Float>>() {
            @Override
            public void onResponse(Call<List<Float>> call, Response<List<Float>> response) {
                apiCallBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Float>> call, Throwable throwable) {
                throwable.printStackTrace();
                apiCallBack.onError();
            }
        });
    }
}
