package com.chex.tracer.api.models;

import java.sql.Timestamp;

public class Videogame {
    private int id;
    private String title;
    private Timestamp released;
    private String image;
    private int metacritic_rate;

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

    public int getMetacritic_rate() {
        return metacritic_rate;
    }

    public void setMetacritic_rate(int metacritic_rate) {
        this.metacritic_rate = metacritic_rate;
    }
}
