package com.chex.tracer.adapters.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chex.tracer.R;
import com.chex.tracer.api.models.Videogame;

import java.util.List;

public class VideogamesHorizontalAdapter extends RecyclerView.Adapter<VideogamesHorizontalAdapter.ViewHolder> {
    private List<Videogame> videogames;
    public VideogamesHorizontalAdapter(List<Videogame> videogames){
        this.videogames = videogames;
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(@NonNull View itemView){
            super(itemView);

        }

        public void bind(Videogame videogame){

        }
    }
}
