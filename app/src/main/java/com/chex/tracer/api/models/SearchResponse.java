package com.chex.tracer.api.models;

import com.chex.tracer.api.models.User;
import com.chex.tracer.api.models.Videogame;

import java.util.List;

public class SearchResponse {
    private List<User> users;
    private List<Videogame> games;
    private List<Review> reviews;

    public SearchResponse(){}

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Videogame> getGames() {
        return games;
    }

    public void setGames(List<Videogame> games) {
        this.games = games;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
