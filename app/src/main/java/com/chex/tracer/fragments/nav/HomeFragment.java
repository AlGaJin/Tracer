package com.chex.tracer.fragments.nav;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chex.tracer.R;
import com.chex.tracer.adapters.viewpager.ScreenSlideVPAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager2 viewPager2 = v.findViewById(R.id.viewPager);
        ScreenSlideVPAdapter adapter = new ScreenSlideVPAdapter(getActivity());
        viewPager2.setAdapter(adapter);

        TabLayout tabLayout = v.findViewById(R.id.tabContainer);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            String[] tabNames = new String[]{
                    getString(R.string.videogames),
                    getString(R.string.reviews)
            };
            tab.setText(tabNames[position]);
        }).attach();

        return v;
    }
}