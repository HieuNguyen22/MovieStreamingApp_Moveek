package com.example.projectmoveek.model;

public class NotifyModel {
    private String title, paragrapth, time;

    public NotifyModel() {
    }

    public NotifyModel(String title, String paragrapth, String time) {
        this.title = title;
        this.paragrapth = paragrapth;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParagrapth() {
        return paragrapth;
    }

    public void setParagrapth(String paragrapth) {
        this.paragrapth = paragrapth;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
