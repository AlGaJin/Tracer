package com.chex.tracer.api.managers;

import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.models.SearchResponse;
import com.chex.tracer.api.services.ReviewService;
import com.chex.tracer.api.services.SearchService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchManager extends BaseManager{
    private final SearchService searchService;

    public SearchManager(){
        super();
        searchService = retrofit.create(SearchService.class);
    }

    public void search(String query, int userId, final APICallBack apiCallBack){
        try{
            JSONObject params = new JSONObject();
            params.put("userId", userId);
            searchService.search(query, createJSONRequestBody(params)).enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    apiCallBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable throwable) {
                    throwable.printStackTrace();
                    apiCallBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
