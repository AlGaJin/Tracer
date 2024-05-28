package com.chex.tracer.adapters.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.chex.tracer.fragments.others.ReviewFragment;
import com.chex.tracer.fragments.tab.ReviewsTabFragment;
import com.chex.tracer.fragments.tab.VideogamesTabFragment;

public class ScreenSlideVPAdapter extends FragmentStateAdapter {
    private Fragment[] fragments = new Fragment[]{
            new VideogamesTabFragment(),
            new ReviewsTabFragment()
    };

    public ScreenSlideVPAdapter(FragmentActivity fgt){
        super(fgt);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
