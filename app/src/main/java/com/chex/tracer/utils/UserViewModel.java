package com.chex.tracer.utils;

import androidx.lifecycle.ViewModel;

import com.chex.tracer.api.models.User;

public class UserViewModel extends ViewModel {
    private User loggedUser;

    public void setLoggedUser(User user){
        loggedUser = user;
    }

    public User getLoggedUser(){
        return loggedUser;
    }
}
