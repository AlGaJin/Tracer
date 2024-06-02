package com.chex.tracer.fragments.nav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chex.tracer.R;
import com.chex.tracer.activities.LogInActivity;
import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.adapters.viewpager.HomeVPAdapter;
import com.chex.tracer.adapters.viewpager.UserVPAdapter;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.managers.UserManager;
import com.chex.tracer.api.models.User;
import com.chex.tracer.fragments.others.EditProfileFragment;
import com.chex.tracer.utils.UserViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class ProfileFragment extends Fragment {
    private final UserManager userManager = new UserManager();
    private TextView usernameTxtV, descrTxtV, followingTxtV, followersTxtV;
    private ImageView profilePicImgV;
    private Button followBtn;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTxtV = v.findViewById(R.id.fgt_profile_usernameTxtView);
        descrTxtV = v.findViewById(R.id.descrTxtV);
        followingTxtV = v.findViewById(R.id.followingTxtV);
        followersTxtV = v.findViewById(R.id.followersTxtV);
        profilePicImgV = v.findViewById(R.id.fgt_profile_userProfilePic);
        followBtn = v.findViewById(R.id.followBtn);

        whoIsIt();

        ViewPager2 viewPager2 = v.findViewById(R.id.user_viewPager);
        UserVPAdapter adapter = new UserVPAdapter(getActivity(), user);
        viewPager2.setAdapter(adapter);

        TabLayout tabLayout = v.findViewById(R.id.user_tabContainer);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            String[] tabNames = new String[]{
                    getString(R.string.videogames),
                    getString(R.string.reviews)
            };
            tab.setText(tabNames[position]);
        }).attach();

        return v;
    }

    private void whoIsIt() {
        Bundle bundle = getArguments();
        User loggedUser = new ViewModelProvider(requireActivity()).get(UserViewModel.class).getLoggedUser();
        if(bundle != null && bundle.containsKey("user") && ((User)bundle.getParcelable("user")).getId() != loggedUser.getId()){
            user = bundle.getParcelable("user");
            followBtn.setVisibility(View.VISIBLE);
            followBtn.setOnClickListener(view -> changeFollowStatus());
        }else{
            user = loggedUser;
            profilePicImgV.setOnClickListener(view -> ((MainActivity)requireActivity()).changeFragmentWithBundle(3, null));
        }

        setUserMediaData();
    }



    public void changeFollowStatus(){
        int followerId = new ViewModelProvider(requireActivity()).get(UserViewModel.class).getLoggedUser().getId();
        int followedId = user.getId();
        int totalFollowers = Integer.parseInt(followersTxtV.getText().toString());

        if(followBtn.getText().equals(getString(R.string.follow))){
            followBtn.setText(R.string.following);
            followersTxtV.setText(String.valueOf(totalFollowers+1));
        }else{
            followBtn.setText(R.string.follow);
            followersTxtV.setText(String.valueOf(totalFollowers-1));
        }

        userManager.changeFollowStatus(followerId, followedId);
    }

    private void setUserMediaData(){
        usernameTxtV.setText(user.getUsername());
        descrTxtV.setText(user.getDescr());
        try {
            int drawableId = R.drawable.class.getField(user.getProfile_pic()).getInt(null);
            profilePicImgV.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawableId, null));
        }catch (Exception e){
            e.printStackTrace();
        }

        int loggedUserId = new ViewModelProvider(requireActivity()).get(UserViewModel.class).getLoggedUser().getId();
        userManager.getSocialMediaData(user.getId(), loggedUserId,new APICallBack() {
            @Override
            public void onSuccess(Object obj) {
                List<String> socialData = (List<String>) obj;
                followersTxtV.setText(socialData.get(0));
                followingTxtV.setText(socialData.get(1));
                if(user.getId() != loggedUserId && socialData.get(3).equals("true")){
                    followBtn.setText(R.string.following);
                }
            }

            @Override
            public void onError() {}
        });
    }
}