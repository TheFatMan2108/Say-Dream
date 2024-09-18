package com.thuydev.saydream.Model;

public class Shoe {
    private int resouceId;
    private String aNew;
    private String price;
    private String name;

    public Shoe(int resouceId, String aNew, String price, String name) {
        this.resouceId = resouceId;
        this.aNew = aNew;
        this.price = price;
        this.name = name;
    }

    public int getResouceId() {
        return resouceId;
    }

    public void setResouceId(int resouceId) {
        this.resouceId = resouceId;
    }

    public String getNew() {
        return aNew;
    }

    public void setNew(String aNew) {
        this.aNew = aNew;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
