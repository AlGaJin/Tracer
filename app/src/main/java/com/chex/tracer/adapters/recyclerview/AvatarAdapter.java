package com.chex.tracer.adapters.recyclerview;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chex.tracer.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {
    private List<Integer> avatarsDrawableId;
    private BottomSheetDialog parentBtmDialog;
    private ImageView parentImgV;
    public AvatarAdapter(List<Integer> avatarsDrawableId, BottomSheetDialog parentBtmDialog, ImageView parentImgV){
        this.avatarsDrawableId = avatarsDrawableId;
        this.parentBtmDialog = parentBtmDialog;
        this.parentImgV = parentImgV;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        ViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.avatarImgV);
        }

        public void bind(Integer avatarId){
            Drawable avatar = ResourcesCompat.getDrawable(itemView.getResources(), avatarId, null);
            imageView.setImageDrawable(avatar);
            imageView.setOnClickListener(view -> {
                parentImgV.setImageDrawable(avatar);
                parentImgV.setTag(avatarId);
                parentBtmDialog.hide();
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items_avatar, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(avatarsDrawableId.get(position));
    }

    @Override
    public int getItemCount() {
        return avatarsDrawableId.size();
    }
}
