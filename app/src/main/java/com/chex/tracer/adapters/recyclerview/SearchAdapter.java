package com.chex.tracer.adapters.recyclerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chex.tracer.R;
import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.api.models.Review;
import com.chex.tracer.api.models.User;
import com.chex.tracer.api.models.Videogame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> users;
    private List<Videogame> games;
    private List<Review> reviews;
    private TextView nothingFoundTxtV;
    private List<Object> filtered = new ArrayList<>();
    private int filterType = 0;// Filtra según el tabLayout seleccionado
                            // 0 -> todos - 1 -> Juegos - 2 -> Usuarios 3-> Reseñas

    public SearchAdapter(TextView nothingFoundTxtV) {
        this.nothingFoundTxtV = nothingFoundTxtV;
        users = new ArrayList<>();
        games = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        private ImageView profilePicImgV;
        private TextView usernameTxtV;

        UserHolder(@NonNull View itemView) {
            super(itemView);
            profilePicImgV = itemView.findViewById(R.id.searchItem_userPicImgV);
            usernameTxtV = itemView.findViewById(R.id.searchItem_usernameTxtV);
        }

        private void bind(User user) {
            try {
                int drawableId = R.drawable.class.getField(user.getProfile_pic()).getInt(null);
                profilePicImgV.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), drawableId, null));
                usernameTxtV.setText(user.getUsername());

                itemView.findViewById(R.id.searchItem_userLayout).setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("user", user);
                    ((MainActivity)itemView.getContext()).changeFragmentWithBundle(R.id.nav_profile, bundle);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class GameHolder extends RecyclerView.ViewHolder {
        private ImageView gameImgV;
        private TextView gameTitleTxtV, gameReleasedTxtV;
        private LinearLayout gameLyt;

        GameHolder(@NonNull View itemView) {
            super(itemView);
            gameImgV = itemView.findViewById(R.id.searchItem_gameImgV);
            gameTitleTxtV = itemView.findViewById(R.id.searchItem_gameTitleTxtV);
            gameReleasedTxtV = itemView.findViewById(R.id.searchItem_releasedTxtV);
            gameLyt = itemView.findViewById(R.id.searchItem_gameLyt);
        }

        private void bind(Videogame game) {
            if (game.getImage() != null) {
                ImageLoader imageLoader = Coil.imageLoader(itemView.getContext());
                ImageRequest request = new ImageRequest.Builder(itemView.getContext()).data(game.getImage()).crossfade(true).target(gameImgV).build();
                imageLoader.enqueue(request);
            }

            gameTitleTxtV.setText(game.getTitle());

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(game.getReleased());
            gameReleasedTxtV.setText(String.valueOf(calendar.get(Calendar.YEAR)));

            gameLyt.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("videogame", game);
                ((MainActivity)itemView.getContext()).changeFragmentWithBundle(1, bundle);
            });
        }
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        private boolean isCollapsed = true;
        private ImageView profilePic, gameImgV;
        private TextView gameTitleTxtV, usernameTxtV, creationTxtV, reviewTxtV;
        private RatingBar ratingBar;

        ReviewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.reviewRV_profilePicImgV);
            gameImgV = itemView.findViewById(R.id.reviewRV_gameImgV);

            gameTitleTxtV = itemView.findViewById(R.id.reviewRV_gameTitleTxtV);
            usernameTxtV = itemView.findViewById(R.id.reviewRV_usernameTxtV);
            creationTxtV = itemView.findViewById(R.id.reviewRV_creationTxtV);
            reviewTxtV = itemView.findViewById(R.id.reviewRV_reviewTxtV);

            ratingBar = itemView.findViewById(R.id.reviewRV_ratingBar);
        }

        public void bind(Review reviewData) {
            setData(reviewData);

            gameImgV.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("videogame", reviewData.getVideogame());
                ((MainActivity) itemView.getContext()).changeFragmentWithBundle(1, bundle);
            });

            reviewTxtV.setOnClickListener(view -> toggleCollapse());
        }

        private void setData(Review reviewData) {
            try {
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

                SimpleDateFormat dateFormater = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                creationTxtV.setText(dateFormater.format(new Date(reviewData.getCreation_date().getTime())));

                ratingBar.setRating(reviewData.getRate());

                reviewTxtV.setText(reviewData.getReview());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void toggleCollapse() {
            if (isCollapsed) {
                reviewTxtV.setMaxLines(Integer.MAX_VALUE);
            } else {
                reviewTxtV.setMaxLines(5);
            }
            isCollapsed = !isCollapsed;
        }
    }

    public void updateAdapter(List<User> users, List<Videogame> games, List<Review> reviews){
        this.users = users;
        this.games = games;
        this.reviews = reviews;
        setFilter(filterType);
    }

    public void setFilter(int filter){
        filterType = filter;
        filtered.clear();
        switch (filterType){
            case 0:
                filtered.addAll(users);
                filtered.addAll(games);
                filtered.addAll(reviews);
                shuffle();
                break;
            case 1:
                filtered.addAll(games);
                break;
            case 2:
                filtered.addAll(users);
                break;
            case 3:
                filtered.addAll(reviews);
                break;
        }
        if(filtered.isEmpty()){
            nothingFoundTxtV.setVisibility(View.VISIBLE);
        }else{
            nothingFoundTxtV.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }

    private void shuffle(){
        final Random rand = new Random(20030507);
        for (int i = 0; i < filtered.size()-1; i++) {
            int randomIndex = rand.nextInt(i+1);
            Object randomObj = filtered.get(randomIndex);
            filtered.set(randomIndex, filtered.get(i));
            filtered.set(i,randomObj);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(filterType == 1){
            return 1;
        } else if (filterType == 2) {
            return 0;
        } else if (filterType == 3) {
            return 2;
        }else{
            Object item = filtered.get(position);
            if(item instanceof User){
                return 0;
            }else if(item instanceof Videogame){
                return 1;
            }else{
                return 2;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new UserHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items_search_user, parent, false));
        } else if (viewType == 1) {
            return new GameHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items_search_game, parent, false));
        }
        return new ReviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 0) {
            ((UserHolder) holder).bind((User) filtered.get(position));
        } else if (holder.getItemViewType() == 1) {
            ((GameHolder) holder).bind((Videogame) filtered.get(position));
        } else {
            ((ReviewHolder) holder).bind((Review) filtered.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }
}
