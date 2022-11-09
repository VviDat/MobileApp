package com.example.bt_quatrinh_2;

import android.graphics.drawable.Drawable;

public class note {
    private String id;
    private String name;
    private String description;
    private String img;
    private String date;
    private String timer;


    public note(String id, String name, String description, String img, String date, String timer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.img = img;
        this.date = date;
        this.timer = timer;
    }

    public note() {

    }

    public String getName() {
        return name;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }
}
