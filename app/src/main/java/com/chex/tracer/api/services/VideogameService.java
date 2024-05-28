package com.chex.tracer.api.services;

import com.chex.tracer.api.models.Platform;
import com.chex.tracer.api.models.Store;
import com.chex.tracer.api.models.Videogame;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VideogameService {

    //Llamadas GET
    @GET(StructureService.BASE_VG)
    Call<List<Videogame>> getAllVideogames();
    @GET(StructureService.GET_LATEST_VG)
    Call<List<Videogame>> getLatestVideogames();
    @GET(StructureService.GET_VG_BY_ID)
    Call<List<Videogame>> getVideogameById(@Path("id") int vgId);
    @GET(StructureService.GET_VG_STORES)
    Call<List<Store>> getVideogameStores(@Path("id") int vgId);
    @GET(StructureService.GET_VG_PLATFORMS)
    Call<List<Platform>> getVideogamesPlatforms(@Path("id") int vgId);
    @GET(StructureService.GET_VG_RATINGS)
    Call<List<Float>> getVideogamesRatings(@Path("id") int vgId);

}
