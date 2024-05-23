package com.chex.tracer.api.managers;

import com.chex.tracer.api.services.StructureService;
import com.chex.tracer.api.utils.NullOnEmptyConverterFactory;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseManager {
    protected Retrofit retrofit;

    protected BaseManager(){
        retrofit = new Retrofit.Builder()
                .baseUrl(StructureService.BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    protected RequestBody createJSONRequestBody(JSONObject params){
        return RequestBody.create(
                params.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );
    }
}
