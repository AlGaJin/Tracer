package com.chex.tracer.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.sql.Timestamp;

public class Review implements Parcelable {
    private User user;
    private Videogame videogame;
    private float rate;
    private String review;
    private Timestamp creation_date;

    public Review(){}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Videogame getVideogame() {
        return videogame;
    }

    public void setVideogame(Videogame videogame) {
        this.videogame = videogame;
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
        user = in.readParcelable(User.class.getClassLoader());
        videogame = in.readParcelable(Videogame.class.getClassLoader());
        rate = in.readFloat();
        review = in.readString();
        creation_date = new Timestamp(in.readLong());
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int i) {
        dest.writeParcelable(user, i);
        dest.writeParcelable(videogame, i);
        dest.writeFloat(rate);
        dest.writeString(review);
        dest.writeLong(creation_date != null ? creation_date.getTime() : -1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
