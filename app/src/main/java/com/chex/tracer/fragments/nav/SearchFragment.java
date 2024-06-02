package com.chex.tracer.fragments.nav;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chex.tracer.R;
import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.adapters.recyclerview.SearchAdapter;
import com.chex.tracer.adapters.viewpager.HomeVPAdapter;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.managers.SearchManager;
import com.chex.tracer.api.models.SearchResponse;
import com.chex.tracer.utils.UserViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class SearchFragment extends Fragment {

    private final SearchManager searchManager = new SearchManager();

    private EditText searchEditTxt;
    private TabLayout tabLayout;
    private Handler handler = new Handler();
    private Runnable runnable;
    private ProgressBar pb;
    private TextView nothingFound;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        pb = v.findViewById(R.id.searchProgressBar);
        nothingFound = v.findViewById(R.id.nothingFoundTxtV);
        int userId = new ViewModelProvider(requireActivity()).get(UserViewModel.class).getLoggedUser().getId();

        searchEditTxt = v.findViewById(R.id.homemadeSearchViewEditTxt);
        tabLayout = v.findViewById(R.id.searchTabContainer);
        RecyclerView rv = v.findViewById(R.id.searchRV);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        SearchAdapter adapter = new SearchAdapter(nothingFound);
        rv.setAdapter(adapter);

        searchEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                nothingFound.setVisibility(View.GONE);
                runnable = () ->{
                    String query = searchEditTxt.getText().toString().trim();
                    if(!query.isEmpty()) {
                        pb.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.VISIBLE);
                        searchManager.search(query, userId, new APICallBack() {
                            @Override
                            public void onSuccess(Object obj) {
                                SearchResponse res = (SearchResponse) obj;

                                ((SearchAdapter)rv.getAdapter()).updateAdapter(res.getUsers(), res.getGames(), res.getReviews());
                                pb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {}
                        });
                    }else{
                        rv.setVisibility(View.GONE);
                        nothingFound.setVisibility(View.GONE);
                    }
                };
                handler.postDelayed(runnable, 250);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handler.removeCallbacks(runnable);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.setFilter(tab.getPosition());
                if(searchEditTxt.getText().toString().trim().isEmpty()){
                    nothingFound.setVisibility(View.GONE);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        tabLayout.setupWithViewPager(new ViewPager(requireContext()));
        return v;
    }
}