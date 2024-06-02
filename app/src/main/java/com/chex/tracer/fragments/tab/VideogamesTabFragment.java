package com.chex.tracer.fragments.tab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chex.tracer.R;
import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.adapters.recyclerview.GameCardViewAdapter;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.managers.VideogameManager;
import com.chex.tracer.api.models.Videogame;
import com.chex.tracer.utils.DisallowParentSwipeOnItemTouchListener;
import com.chex.tracer.utils.UserViewModel;

import java.util.List;


public class VideogamesTabFragment extends Fragment {

    private final VideogameManager vgManager = new VideogameManager();

    private RecyclerView latestGamesRV, friendsActivityRV;
    private TextView seeAllTxtV, friendsActivityTxtV;
    private ProgressBar pb;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment_videogame, container, false);

        pb = v.findViewById(R.id.gamesTabProgressBar);
        latestGamesRV = v.findViewById(R.id.latestGamesRV);
        friendsActivityRV = v.findViewById(R.id.friendsActivityRV);
        seeAllTxtV = v.findViewById(R.id.seeAllTxtV);
        friendsActivityTxtV = v.findViewById(R.id.friendsActivityTxtV);
        swipeRefresh = v.findViewById(R.id.gamesTab_swipeRefresh);

        LinearLayoutManager latestLytMngr = new LinearLayoutManager(getContext());
        latestLytMngr.setOrientation(RecyclerView.HORIZONTAL);
        latestGamesRV.setLayoutManager(latestLytMngr);
        latestGamesRV.addOnItemTouchListener(new DisallowParentSwipeOnItemTouchListener());

        seeAllTxtV.setOnClickListener(view -> {
            ((MainActivity)requireActivity()).changeFragmentWithBundle(4, null);
        });

        LinearLayoutManager friendsLytMgr = new LinearLayoutManager(getContext());
        friendsLytMgr.setOrientation(RecyclerView.HORIZONTAL);
        friendsActivityRV.setLayoutManager(friendsLytMgr);
        friendsActivityRV.addOnItemTouchListener(new DisallowParentSwipeOnItemTouchListener());

        swipeRefresh.setOnRefreshListener(this::updateLists);

        updateLists();
        return v;
    }

    private void updateLists(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        int userId = Integer.parseInt(pref.getString("userId", "4"));
        vgManager.getFriendsActivity(userId, new APICallBack() {
            @Override
            public void onSuccess(Object obj) {
                List<Videogame> friendsActivityList = (List<Videogame>) obj;

                if(friendsActivityList == null || friendsActivityList.isEmpty()){
                    friendsActivityTxtV.setVisibility(View.GONE);
                    friendsActivityRV.setVisibility(View.GONE);
                }else{
                    friendsActivityRV.setAdapter(new GameCardViewAdapter(friendsActivityList));
                }

                pb.setVisibility(View.GONE);
                if(swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);

            }

            @Override
            public void onError() {}
        });

        vgManager.getLatest(new APICallBack() {
            @Override
            public void onSuccess(Object obj) {
                latestGamesRV.setAdapter(new GameCardViewAdapter((List<Videogame>) obj));

                pb.setVisibility(View.GONE);
                if(swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onError() {

            }
        });
    }

}