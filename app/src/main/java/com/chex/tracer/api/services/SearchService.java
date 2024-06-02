package com.chex.tracer.api.services;

import com.chex.tracer.api.models.SearchResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SearchService {
    @POST(StructureService.SEARCH)
    Call<SearchResponse> search(@Query("query") String query, @Body RequestBody params);
}

