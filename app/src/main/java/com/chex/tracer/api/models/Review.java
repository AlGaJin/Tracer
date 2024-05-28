package com.chex.tracer.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.sql.Timestamp;

public class Review implements Parcelable {
    private int user_id;
    private int videogame_id;
    private float rate;
    private String review;
    private Timestamp creation_date;

    public Review(){}

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getVideogame_id() {
        return videogame_id;
    }

    public void setVideogame_id(int videogame_id) {
        this.videogame_id = videogame_id;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Timestamp getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Timestamp creation_date) {
        this.creation_date = creation_date;
    }

    // Métodos Parcelable
    protected Review(Parcel in) {
        user_id = in.readInt();
        videogame_id = in.readInt();
        rate = in.readFloat();
        review = in.readString();
        creation_date = new Timestamp(in.readLong());
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int i) {
        dest.writeInt(user_id);
        dest.writeInt(videogame_id);
        dest.writeFloat(rate);
        dest.writeString(review);
        dest.writeLong(creation_date != null ? creation_date.getTime() : -1);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
