package com.thuydev.saydream.Model;

public class Shoe {
    private int ResouceId;
    private String New;
    private String Price;
    private String Name;

    public Shoe(int resouceId, String aNew, String price, String name) {
        ResouceId = resouceId;
        New = aNew;
        Price = price;
        Name = name;
    }

    public int getResouceId() {
        return ResouceId;
    }

    public void setResouceId(int resouceId) {
        ResouceId = resouceId;
    }

    public String getNew() {
        return New;
    }

    public void setNew(String aNew) {
        New = aNew;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
