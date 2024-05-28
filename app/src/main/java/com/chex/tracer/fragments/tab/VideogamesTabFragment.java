package com.chex.tracer.fragments.tab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chex.tracer.R;
import com.chex.tracer.adapters.recyclerview.GamesCardViewAdapter;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.managers.VideogameManager;
import com.chex.tracer.api.models.Videogame;
import com.chex.tracer.utils.DisallowParentSwipeOnItemTouchListener;

import java.util.List;


public class VideogamesTabFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_videogame_tab, container, false);

        RecyclerView latestGamesRV = v.findViewById(R.id.latestGamesRV);
        LinearLayoutManager myLytMgr = new LinearLayoutManager(getContext());
        myLytMgr.setOrientation(RecyclerView.HORIZONTAL);
        latestGamesRV.setLayoutManager(myLytMgr);
        new VideogameManager().getLatest(new APICallBack() {
            @Override
            public void onSuccess(Object obj) {
                latestGamesRV.setAdapter(new GamesCardViewAdapter((List<Videogame>) obj));
            }

            @Override
            public void onError() {

            }
        });
        latestGamesRV.addOnItemTouchListener(new DisallowParentSwipeOnItemTouchListener());

        //RecyclerView friendsActivityRV = v.findViewById(R.id.friendsActivity);



        return v;
    }
}