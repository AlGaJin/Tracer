package com.chex.tracer.fragments.tab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chex.tracer.R;
import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.api.managers.VideogameManager;

public class UserListTabFragment extends Fragment {

    private final VideogameManager vgManager = new VideogameManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment_user_list, container, false);

        v.findViewById(R.id.playedBtn).setOnClickListener(view -> {
            seeGameList("played");
        });

        v.findViewById(R.id.playingBtn).setOnClickListener(view -> {
            seeGameList("playing");
        });

        v.findViewById(R.id.backlogBtn).setOnClickListener(view -> {
            seeGameList("backlog");
        });
        return v;
    }

    private void seeGameList(String list){
        Bundle bundle = getArguments();
        bundle.putString("list", list);
        ((MainActivity)requireActivity()).changeFragmentWithBundle(4, bundle);
    }
}