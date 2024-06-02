package com.chex.tracer.fragments.others;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chex.tracer.R;
import com.chex.tracer.adapters.recyclerview.GameGalleryAdapter;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.managers.VideogameManager;
import com.chex.tracer.api.models.User;
import com.chex.tracer.api.models.Videogame;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

public class GameGalleryFragment extends Fragment {
    private final VideogameManager videogameManager = new VideogameManager();
    private EditText searchEditTxt;
    private RecyclerView rv;
    private ProgressBar pb;
    private TextView nothingFound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_gallery, container, false);

        searchEditTxt = v.findViewById(R.id.gameGalleryEditTxt);
        pb = v.findViewById(R.id.galleryProgressBar);
        nothingFound = v.findViewById(R.id.gallery_nothingFoundTxtV);

        rv = v.findViewById(R.id.gameGalleryRV);
        FlexboxLayoutManager manager = new FlexboxLayoutManager(getContext(), FlexDirection.ROW);
        rv.setLayoutManager(manager);

        setData();

        searchEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String query = searchEditTxt.getText().toString().trim();
                ((GameGalleryAdapter)rv.getAdapter()).filter(query);
            }

            //Estos dos métodos son obligatorios de implementar, sin embargo no son necsarios
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });
        return v;
    }

    private void setData() {
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey("user")){
            int userId = ((User)bundle.getParcelable("user")).getId();
            String list = bundle.getString("list");

            videogameManager.getUserGameList(userId, list, new APICallBack() {
                @Override
                public void onSuccess(Object obj) {
                    List<Videogame> games = (List<Videogame>) obj;
                    if(games.isEmpty()){
                        nothingFound.setText(R.string.there_is_nothing_to_see_here);
                        nothingFound.setVisibility(View.VISIBLE);
                    }
                    rv.setAdapter(new GameGalleryAdapter((List<Videogame>) obj, nothingFound, bundle));
                    pb.setVisibility(View.GONE);
                }

                @Override
                public void onError() {}
            });
        }else{
            videogameManager.getAll(new APICallBack() {
                @Override
                public void onSuccess(Object obj) {
                    rv.setAdapter(new GameGalleryAdapter((List<Videogame>) obj, nothingFound, new Bundle()));
                    pb.setVisibility(View.GONE);
                }

                @Override
                public void onError() {}
            });
        }
    }
}