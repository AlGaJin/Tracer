package com.chex.tracer.adapters.recyclerview;

import android.animation.LayoutTransition;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chex.tracer.R;
import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.api.models.Review;
import com.chex.tracer.api.models.User;
import com.chex.tracer.api.models.Videogame;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> reviews;
    public ReviewAdapter(List<Review> reviews){
        this.reviews = reviews;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private boolean isCollapsed = true;
        private ImageView profilePic, gameImgV;
        private TextView gameTitleTxtV, usernameTxtV, creationTxtV, reviewTxtV;
        private RatingBar ratingBar;
        ViewHolder(@NonNull View itemView){
            super(itemView);
            profilePic = itemView.findViewById(R.id.reviewRV_profilePicImgV);
            gameImgV = itemView.findViewById(R.id.reviewRV_gameImgV);

            gameTitleTxtV = itemView.findViewById(R.id.reviewRV_gameTitleTxtV);
            usernameTxtV = itemView.findViewById(R.id.reviewRV_usernameTxtV);
            creationTxtV = itemView.findViewById(R.id.reviewRV_creationTxtV);
            reviewTxtV = itemView.findViewById(R.id.reviewRV_reviewTxtV);

            ratingBar  = itemView.findViewById(R.id.reviewRV_ratingBar);
        }

        public void bind(Review reviewData){
            setData(reviewData);

            gameImgV.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("videogame", reviewData.getVideogame());
                ((MainActivity)itemView.getContext()).changeFragmentWithBundle(1, bundle);
            });

            reviewTxtV.setOnClickListener(view -> collapsaitor());
        }

        private void setData(Review reviewData){
            try{
                User user = reviewData.getUser();
                Videogame game = reviewData.getVideogame();

                int drawableId = R.drawable.class.getField(user.getProfile_pic()).getInt(null);
                profilePic.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), drawableId, null));
                usernameTxtV.setText(user.getUsername());

                gameTitleTxtV.setText(game.getTitle());
                if (game.getImage() != null) {
                    ImageLoader imageLoader = Coil.imageLoader(itemView.getContext());
                    ImageRequest request = new ImageRequest.Builder(itemView.getContext()).data(game.getImage()).crossfade(true).target(gameImgV).build();
                    imageLoader.enqueue(request);
                }

                SimpleDateFormat dateFormater= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                creationTxtV.setText(dateFormater.format(new Date(reviewData.getCreation_date().getTime())));

                ratingBar.setRating(reviewData.getRate());

                reviewTxtV.setText(reviewData.getReview());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void collapsaitor(){
            if(isCollapsed){
                reviewTxtV.setMaxLines(Integer.MAX_VALUE);
            }else{
                reviewTxtV.setMaxLines(5);
            }
            isCollapsed = !isCollapsed;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items_review, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}