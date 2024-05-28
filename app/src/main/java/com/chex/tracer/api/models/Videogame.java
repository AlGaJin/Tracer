package com.chex.tracer.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.sql.Timestamp;

public class Videogame implements Parcelable {
    private int id;
    private String title;
    private Timestamp released;
    private String image;
    private Integer metacritic_rate;
    private String descr;

    public Videogame(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getReleased() {
        return released;
    }

    public void setReleased(Timestamp released) {
        this.released = released;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getMetacritic_rate() {
        return metacritic_rate;
    }

    public void setMetacritic_rate(Integer metacritic_rate) {
        this.metacritic_rate = metacritic_rate;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }


    // Métodos Parcelable
    protected Videogame(Parcel in) {
        id = in.readInt();
        title = in.readString();
        released = new Timestamp(in.readLong());
        image = in.readString();
        metacritic_rate = in.readInt();
        descr = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeLong(released != null ? released.getTime() : -1);
        dest.writeString(image);
        dest.writeInt(metacritic_rate);
        dest.writeString(descr);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
