package com.example.projectmoveek.model;

public class UserModel {
    private int id;
    private String fID, email, password, fullname, phoneNum, avatar, bio;

    public UserModel() {
    }

    public UserModel(int id, String fID, String email, String password, String fullname, String phoneNum, String avatar, String bio) {
        this.id = id;
        this.fID = fID;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.phoneNum = phoneNum;
        this.avatar = avatar;
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
