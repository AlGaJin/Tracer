package com.chex.tracer.adapters.viewpager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.chex.tracer.api.models.User;
import com.chex.tracer.fragments.tab.ReviewsTabFragment;
import com.chex.tracer.fragments.tab.UserListTabFragment;
import com.chex.tracer.fragments.tab.VideogamesTabFragment;

import java.util.ArrayList;
import java.util.List;

public class UserVPAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragments = new ArrayList<>();

    public UserVPAdapter(FragmentActivity fgt, User user){
        super(fgt);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        ReviewsTabFragment reviewsTabFragment = new ReviewsTabFragment();
        reviewsTabFragment.setArguments(bundle);
        UserListTabFragment userListTabFragment = new UserListTabFragment();
        userListTabFragment.setArguments(bundle);
        fragments.add(userListTabFragment);
        fragments.add(reviewsTabFragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
