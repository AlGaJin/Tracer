package com.chex.tracer.fragments.tab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chex.tracer.R;
import com.chex.tracer.adapters.recyclerview.ReviewAdapter;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.managers.ReviewManager;
import com.chex.tracer.api.models.Review;
import com.chex.tracer.api.models.User;

import java.util.List;

public class ReviewsTabFragment extends Fragment {

    private final ReviewManager reviewManager = new ReviewManager();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv;
    private TextView nothingToSeeTxtV;
    private ProgressBar pb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment_reviews, container, false);

        nothingToSeeTxtV = v.findViewById(R.id.review_nothingTxtV);
        pb = v.findViewById(R.id.reviewProgressBar);

        swipeRefreshLayout = v.findViewById(R.id.reviewTab_swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getLatestReviews();
        });

        rv = v.findViewById(R.id.latestReviewRV);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle bundle = getArguments();
        if(bundle != null){
            swipeRefreshLayout.setEnabled(false);
            int userId = ((User)bundle.getParcelable("user")).getId();
            reviewManager.getUserReviews(userId, new APICallBack() {
                @Override
                public void onSuccess(Object obj) {
                    List<Review> reviews = (List<Review>) obj;
                    if(reviews.isEmpty()){
                        nothingToSeeTxtV.setVisibility(View.VISIBLE);
                    }
                    rv.setAdapter(new ReviewAdapter(reviews));
                    pb.setVisibility(View.GONE);
                }

                @Override
                public void onError() {}
            });
        }else{
            getLatestReviews();
        }
        return v;
    }

    private void getLatestReviews(){
        reviewManager.getLatestReviews(new APICallBack() {
            @Override
            public void onSuccess(Object obj) {
                List<Review> reviews = (List<Review>) obj;

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }

                if(reviews.isEmpty()){
                    nothingToSeeTxtV.setVisibility(View.VISIBLE);
                }

                rv.setAdapter(new ReviewAdapter(reviews));
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onError() {}
        });
    }
}