package com.chex.tracer.api.managers;

import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.models.Review;
import com.chex.tracer.api.services.ReviewService;
import com.chex.tracer.api.services.UserService;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewManager extends BaseManager{
    private final ReviewService reviewService;

    public ReviewManager(){
        super();
        reviewService = retrofit.create(ReviewService.class);
    }

    public void getLatestReviews(final APICallBack apiCallBack){
        reviewService.getLatestReviews().enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                apiCallBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable throwable) {
                throwable.printStackTrace();
                apiCallBack.onError();
            }
        });
    }

    public void getUserReviews(int userId, final APICallBack apiCallBack){
        reviewService.getUserReviews(userId).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                apiCallBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable throwable) {
                throwable.printStackTrace();
                apiCallBack.onError();
            }
        });
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

    public void editReview(int userId, int gameId, float rate, String review) {
        try{
            JSONObject params = new JSONObject();
            params.put("userId", userId);
            params.put("gameId", gameId);
            params.put("rate", rate);
            params.put("review", review);

            reviewService.editReview(createJSONRequestBody(params)).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {}

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {throwable.printStackTrace();}
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
