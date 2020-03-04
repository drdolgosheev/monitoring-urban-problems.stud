package com.example.monitoringurbanproblems;

import android.content.Context;

import com.google.firebase.database.annotations.Nullable;

public class Problem {
    private double id = 0;
    private double latitude = 0;
    private double longitude = 0;
    private String user_id;
    private String description;
    /*
    1 - не просмотрена
    2 - просмотрена
    3 - утверждена
    4 - решена
     */
    private int status;
    private String img_url;

    public Problem(double latitude, double longitude, String user_id, String description,
                      String img_url, double id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.user_id = user_id;
        this.description = description;
        this.img_url = img_url;
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(User user) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public void addAProblem(Context cur_context, User cur_user) {
        boolean isSucsessful = false;
    }

    public void addImage(Context cur_content, User cur_user) {

    }
}