package com.chex.tracer.fragments.others;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chex.tracer.R;
import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.adapters.recyclerview.AvatarAdapter;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.managers.UserManager;
import com.chex.tracer.api.models.User;
import com.chex.tracer.utils.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {
    private final UserManager userManager = new UserManager();
    private CircleImageView userProfilePic;
    private EditText usernameEditTxt, emailEditTxt, descrEditTxt;
    private User updatedUser;
    private User loggedUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        loggedUser = new ViewModelProvider(requireActivity()).get(UserViewModel.class).getLoggedUser();
        updatedUser = new ViewModelProvider(requireActivity()).get(UserViewModel.class).getLoggedUser().clone();

        userProfilePic = v.findViewById(R.id.edit_userImgV);
        usernameEditTxt = v.findViewById(R.id.edit_usernameEditTxt);
        emailEditTxt = v.findViewById(R.id.edit_emailEditTxt);
        descrEditTxt = v.findViewById(R.id.edit_descrEditText);

        setData();

        v.findViewById(R.id.edit_userPicRelativeLayout).setOnClickListener(view -> showActionBottomSheet());
        v.findViewById(R.id.edit_saveBtn).setOnClickListener(view -> saveChanges());
        return v;
    }

    private void setData() {
        usernameEditTxt.setText(updatedUser.getUsername());
        emailEditTxt.setText(updatedUser.getEmail());
        descrEditTxt.setText(updatedUser.getDescr());

        try{
            int drawableId = R.drawable.class.getField(updatedUser.getProfile_pic()).getInt(null);
            userProfilePic.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawableId, null));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveChanges() {
        if(checkFields()){
            userManager.updateUser(loggedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getDescr(), updatedUser.getProfile_pic(), new APICallBack() {
                @Override
                public void onSuccess(Object obj) {
                    if(obj != null && (Boolean)obj){
                        if(!updatedUser.getUsername().isEmpty()) loggedUser.setUsername(updatedUser.getUsername());
                        if(!updatedUser.getEmail().isEmpty()) loggedUser.setEmail(updatedUser.getEmail());
                        if(!updatedUser.getDescr().isEmpty()) loggedUser.setDescr(updatedUser.getDescr());
                        loggedUser.setProfile_pic(updatedUser.getProfile_pic());
                        ((MainActivity)requireActivity()).setProfilePic(loggedUser.getProfile_pic());
                    }
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    private boolean checkFields(){
        String newUsername = usernameEditTxt.getText().toString().trim();
        String newEmail = emailEditTxt.getText().toString().trim();
        String newDescr = descrEditTxt.getText().toString().trim();
        Integer newDrawableId = (Integer) userProfilePic.getTag();
        if(newDrawableId != null){
            updatedUser.setProfile_pic(getResources().getResourceEntryName(newDrawableId));
        }

        if(!newUsername.isEmpty() && !newUsername.equals(loggedUser.getUsername())){
            updatedUser.setUsername(newUsername);
            userManager.isUsernameAvailable(newUsername, new APICallBack() {
                @Override
                public void onSuccess(Object obj) {
                    if(!(Boolean)obj){
                        usernameEditTxt.setError(getString(R.string.not_available));
                    }
                }

                @Override
                public void onError() {

                }
            });
        }else if(newUsername.equals(loggedUser.getUsername())){
            updatedUser.setUsername("");
        }

        if(Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
            if(!newEmail.isEmpty() && !newEmail.equals(loggedUser.getEmail())){
                updatedUser.setEmail(newEmail);
                userManager.isEmailAvailable(newEmail, new APICallBack() {
                    @Override
                    public void onSuccess(Object obj) {
                        if(!(Boolean)obj){
                            emailEditTxt.setError(getString(R.string.not_available));
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
            }else if(newEmail.equals(loggedUser.getEmail())){
                updatedUser.setEmail("");
            }
        }else{
            emailEditTxt.setError(getString(R.string.invalid_email_format));
            return false;
        }


        if(!newDescr.equals(loggedUser.getDescr())){
            updatedUser.setDescr(newDescr);
        }
        return true;
    }

    private void showActionBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View sheetView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile_pic, null);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();


        RecyclerView rv = sheetView.findViewById(R.id.avatar_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(new AvatarAdapter(getAvatars(), bottomSheetDialog, userProfilePic));

        sheetView.findViewById(R.id.deleteAvatarImgV).setOnClickListener(view -> {
            userProfilePic.setTag(R.drawable.avatar_default);
            userProfilePic.setImageDrawable(ResourcesCompat.getDrawable(
                    view.getResources(), R.drawable.avatar_default, null));
            bottomSheetDialog.dismiss();
        });
    }

    private List<Integer> getAvatars() {
        Field[] drawableFields = R.drawable.class.getFields();
        List<Integer> avatars = new ArrayList<>();
        for (Field f : drawableFields) {
            try{
                if(f.getName().contains("avatar_") && !f.getName().equals("avatar_default")){
                    avatars.add(f.getInt(null));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return avatars;
    }
}