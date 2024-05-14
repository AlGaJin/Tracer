package com.chex.tracer.api.models;

import java.sql.Timestamp;

public class Chat {
    private int id;
    private int user_id;
    private int other_user_id;
    private Timestamp creation_date;

    public Chat(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getOther_user_id() {
        return other_user_id;
    }

    public void setOther_user_id(int other_user_id) {
        this.other_user_id = other_user_id;
    }

    public Timestamp getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Timestamp creation_date) {
        this.creation_date = creation_date;
    }
}
