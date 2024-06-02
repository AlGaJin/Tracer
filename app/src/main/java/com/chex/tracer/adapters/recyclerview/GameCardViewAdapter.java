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

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class GameCardViewAdapter extends RecyclerView.Adapter<GameCardViewAdapter.ViewHolder> {
    private List<Videogame> videogames;
    public GameCardViewAdapter(List<Videogame> videogames){
        this.videogames = videogames;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private ImageView gameImgView;
        private TextView gameTitleTxtV;
        ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.gamesCardView);
            gameImgView = itemView.findViewById(R.id.imageView);
            gameTitleTxtV = itemView.findViewById(R.id.titleTextView);
        }

        public void bind(Videogame videogame){
            String imgUrl = videogame.getImage();
            String title = videogame.getTitle();

            if (imgUrl != null) {
                ImageLoader imageLoader = Coil.imageLoader(itemView.getContext());
                ImageRequest request = new ImageRequest.Builder(itemView.getContext()).data(imgUrl).crossfade(true).target(gameImgView).build();
                imageLoader.enqueue(request);
            }
            gameTitleTxtV.setText(title);
            cardView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("videogame", videogame);
                ((MainActivity)itemView.getContext()).changeFragmentWithBundle(1, bundle);
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items_videogame, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(videogames.get(position));
    }

    @Override
    public int getItemCount() {
        return videogames.size();
    }
}
