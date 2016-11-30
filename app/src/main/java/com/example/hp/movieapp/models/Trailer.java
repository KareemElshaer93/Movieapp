package com.example.hp.movieapp.models;

/**
 * Created by Hp on 25/11/2016.
 */
public class Trailer {
    String name,key;

    public Trailer(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
