package com.chex.tracer.adapters.recyclerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.chex.tracer.R;
import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.api.models.Videogame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class GameGalleryAdapter extends RecyclerView.Adapter<GameGalleryAdapter.ViewHolder> {
    private List<Videogame> videogames;
    private List<Videogame> filtered;
    private TextView nothingFound;
    private Bundle bundle;
    public GameGalleryAdapter(List<Videogame> videogames, TextView nothingFound, Bundle bundle){
        this.videogames = videogames;
        this.nothingFound = nothingFound;
        this.bundle = bundle;
        filtered = new ArrayList<>();
        filtered.addAll(videogames);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private ImageView gameImgView;
        private TextView placeHolderTxtV;
        ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.itemGalleryCardView);
            gameImgView = itemView.findViewById(R.id.itemGalleryImgV);
            placeHolderTxtV = itemView.findViewById(R.id.galleryPlaceHolderTxtV);
        }

        public void bind(Videogame videogame){
            String imgUrl = videogame.getImage();
            placeHolderTxtV.setText(videogame.getTitle());
            if (imgUrl != null) {
                ImageLoader imageLoader = Coil.imageLoader(itemView.getContext());
                ImageRequest request = new ImageRequest.Builder(itemView.getContext()).data(imgUrl).crossfade(true).target(gameImgView).build();
                imageLoader.enqueue(request);
            }
            cardView.setOnClickListener(view -> {
                bundle.putParcelable("videogame", videogame);
                ((MainActivity)itemView.getContext()).changeFragmentWithBundle(1, bundle);
            });
        }
    }

    public void filter(String query){
        filtered.clear();
        if(!query.isEmpty()){
            for(Videogame game: videogames){
                if(game.getTitle().toLowerCase().contains(query.toLowerCase())){
                    filtered.add(game);
                }
            }
        }else{
            filtered.addAll(videogames);
        }

        if(filtered.isEmpty()){
            nothingFound.setVisibility(View.VISIBLE);
        }else{
            nothingFound.setVisibility(View.GONE);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items_gallery_game, parent, false);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        layoutParams.width = (parent.getWidth() / 4) - layoutParams.leftMargin - layoutParams.rightMargin;
        v.setLayoutParams(layoutParams);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(filtered.get(position));
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }
}
