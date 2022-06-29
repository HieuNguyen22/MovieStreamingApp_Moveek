package com.example.projectmoveek.model;

public class HireModel {
    private int hID, mID, cID;
    private String charName;

    public HireModel(int hID, int mID, int cID, String charName) {
        this.hID = hID;
        this.mID = mID;
        this.cID = cID;
        this.charName = charName;
    }

    public int gethID() {
        return hID;
    }

    public void sethID(int hID) {
        this.hID = hID;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public int getcID() {
        return cID;
    }

    public void setcID(int cID) {
        this.cID = cID;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }
}
