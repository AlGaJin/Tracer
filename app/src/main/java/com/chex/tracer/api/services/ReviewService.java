package com.chex.tracer.api.services;

import com.chex.tracer.api.models.Review;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReviewService {
    //Llamadas GET
    @GET(StructureService.BASE_REVIEW)
    Call<List<Review>> getAllReviews();
    @GET(StructureService.GET_USER_REVIEWS)
    Call<List<Review>> getUserReviews(@Path("userId") int userId);
    @GET(StructureService.GET_REVIEW)
    Call<Review> getReview(@Path("gameId") int gameId, @Path("userId") int userId);
    @GET(StructureService.GET_LATEST_REVIEWS)
    Call<List<Review>> getLatestReviews();

    //Llamadas PUT
    @PUT(StructureService.UPDATE_REVIEW)
    Call<Void> editReview(@Body RequestBody params);
}
