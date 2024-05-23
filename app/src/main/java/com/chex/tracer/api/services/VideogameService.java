package com.chex.tracer.api.services;

import com.chex.tracer.api.models.Videogame;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VideogameService {

    @GET(StructureService.BASE_VG)
    Call<List<Videogame>> getAllVideogames();
    @GET(StructureService.GET_LATEST_VG)
    Call<List<Videogame>> getLatestVideogames();
    @GET(StructureService.GET_VG_BY_ID)
    Call<List<Videogame>> getVideogameById();

}
