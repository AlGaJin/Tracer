package com.chex.tracer.fragments.nav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chex.tracer.R;
import com.chex.tracer.activities.LogInActivity;
import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.managers.UserManager;
import com.chex.tracer.api.models.User;
import com.chex.tracer.fragments.others.EditProfileFragment;
import com.chex.tracer.utils.UserViewModel;

import java.util.List;

public class ProfileFragment extends Fragment {
    private final UserManager userManager = new UserManager();
    private TextView usernameTxtV, descrTxtV, followingTxtV, followersTxtV, totalGamesTxtV;
    private ImageView profilePicImgV;
    private Button leftBtn, rightBtn;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTxtV = v.findViewById(R.id.fgt_profile_usernameTxtView);
        descrTxtV = v.findViewById(R.id.descrTxtV);
        followingTxtV = v.findViewById(R.id.followingTxtV);
        followersTxtV = v.findViewById(R.id.followersTxtV);
        totalGamesTxtV = v.findViewById(R.id.totalGamesTxtV);
        profilePicImgV = v.findViewById(R.id.fgt_profile_userProfilePic);
        leftBtn = v.findViewById(R.id.leftBtn);
        rightBtn = v.findViewById(R.id.rightBtn);

        whoIsLogged();

        return v;
    }

    private void whoIsLogged() {
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey("userId")){
            userManager.getUser(bundle.getInt("userId"), new APICallBack() {
                @Override
                public void onSuccess(Object obj) {
                    user = (User) obj;
                    setUserMediaData();
                    isFollowing();
                }

                @Override
                public void onError() {

                }
            });

            rightBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ui_chat, 0);
            rightBtn.setText("");
            leftBtn.setText(R.string.follow);
            leftBtn.setOnClickListener(view -> isFollowing());
        }else{
            user = new ViewModelProvider(requireActivity()).get(UserViewModel.class).getLoggedUser();
            setUserMediaData();
            leftBtn.setOnClickListener(view -> ((MainActivity)requireActivity()).changeFragmentWithBundle(3, null));
            rightBtn.setOnClickListener(view -> logOut());
        }
    }

    public void isFollowing(){
        int followerId = new ViewModelProvider(requireActivity()).get(UserViewModel.class).getLoggedUser().getId();
        int followedId = user.getId();
        userManager.isFollowing(followerId, followedId, new APICallBack() {
            @Override
            public void onSuccess(Object obj) {
                if((Boolean)obj){
                    leftBtn.setText(R.string.following);
                }else{
                    leftBtn.setText(R.string.follow);
                }
            }

            @Override
            public void onError() {

            }
        });
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

        userManager.getSocialMediaData(user.getId(), new APICallBack() {
            @Override
            public void onSuccess(Object obj) {
                List<String> socialData = (List<String>) obj;
                followersTxtV.setText(socialData.get(0));
                followingTxtV.setText(socialData.get(1));
                totalGamesTxtV.setText(socialData.get(2));
            }

            @Override
            public void onError() {

            }
        });
    }

    private void logOut(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId", "");
        editor.apply();

        startActivity(new Intent(this.getContext(), LogInActivity.class));
        requireActivity().finish();
    }
}