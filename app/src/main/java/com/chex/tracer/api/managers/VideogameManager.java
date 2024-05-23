package com.chex.tracer.api.managers;

import android.util.Log;

import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.models.Videogame;
import com.chex.tracer.api.services.VideogameService;

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
}
