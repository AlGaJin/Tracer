package com.chex.tracer.api.managers;

import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.models.Review;
import com.chex.tracer.api.services.ReviewService;
import com.chex.tracer.api.services.UserService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewManager extends BaseManager{
    private final ReviewService reviewService;

    public ReviewManager(){
        super();
        reviewService = retrofit.create(ReviewService.class);
    }

    public void getReview(int gameId, int userId, final APICallBack apiCallBack){
        try{
            JSONObject params = new JSONObject();
            params.put("gameId", gameId);
            params.put("userId", userId);

            reviewService.getReview(gameId, userId).enqueue(new Callback<Review>() {
                @Override
                public void onResponse(Call<Review> call, Response<Review> response) {
                    apiCallBack.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<Review> call, Throwable throwable) {
                    throwable.printStackTrace();
                    apiCallBack.onError();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
