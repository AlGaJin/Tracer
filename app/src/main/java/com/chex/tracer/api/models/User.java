package com.chex.tracer.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.sql.Timestamp;

public class User implements Cloneable, Parcelable {
    private int id;
    private String username;
    private String email;
    private String descr;
    private String profile_pic;

    public User(){}

    @NonNull
    @Override
    public User clone() {
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    //Getter y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", descr='" + descr + '\'' +
                ", profilePic=" + profile_pic +
                '}';
    }

    // Métodos Parcelable
    protected User(Parcel in) {
        id = in.readInt();
        username = in.readString();
        email = in.readString();
        descr = in.readString();
        profile_pic = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(descr);
        dest.writeString(profile_pic);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
