package com.chex.tracer.adapters.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chex.tracer.R;
import com.chex.tracer.api.models.Videogame;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class GamesCardViewAdapter extends RecyclerView.Adapter<GamesCardViewAdapter.ViewHolder> {
    private List<Videogame> videogames;
    public GamesCardViewAdapter(List<Videogame> videogames){
        this.videogames = videogames;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView titleTxtView;
        ViewHolder(@NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            titleTxtView = itemView.findViewById(R.id.titleTextView);

        }

        public void bind(Videogame videogame){
            String imgUrl = videogame.getImage();
            String title = videogame.getTitle();

            if (imgUrl != null) {
                ImageLoader imageLoader = Coil.imageLoader(itemView.getContext());
                ImageRequest request = new ImageRequest.Builder(itemView.getContext()).data(imgUrl).crossfade(true).target(imageView).build();
                imageLoader.enqueue(request);
            }
            titleTxtView.setText(title);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.videogame_item_cardview, parent, false);
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
