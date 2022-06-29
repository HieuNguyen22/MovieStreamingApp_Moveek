package com.example.projectmoveek.model;

public class CastJoinMovieModel {
    private int id;
    private String name, image, charName;

    public CastJoinMovieModel(int id, String name, String image, String charName) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.charName = charName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }
}
