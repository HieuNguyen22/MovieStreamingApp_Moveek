package com.example.projectmoveek.model;

import java.util.List;

public class ItemModel {
    private int id;
    private String title, poster, year, length, type, genre, description;
    private String trailerURL;
    private double imdb;

    public ItemModel() {
    }

    public ItemModel(int id, String title, String poster, String year, String length, String type, String genre, String description, String trailerURL, double imdb) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.year = year;
        this.length = length;
        this.type = type;
        this.genre = genre;
        this.description = description;
        this.trailerURL = trailerURL;
        this.imdb = imdb;
    }

    public String getTrailerURL() {
        return trailerURL;
    }

    public void setTrailerURL(String trailerURL) {
        this.trailerURL = trailerURL;
    }

    public String getInfo(){
        return  length + " | " + genre;
    }

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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getImdb() {
        return imdb;
    }

    public void setImdb(double imdb) {
        this.imdb = imdb;
    }

}
