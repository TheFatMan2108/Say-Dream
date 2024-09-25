package com.thuydev.saydream.DTO;

public class Photo {
    private String url;

    public Photo() {
        // Constructor mặc định cần cho Firebase
    }

    public Photo(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

