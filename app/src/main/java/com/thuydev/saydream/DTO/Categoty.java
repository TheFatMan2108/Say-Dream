package com.thuydev.saydream.DTO;

public class Categoty {
    private String id;
    private String name;

    public Categoty() {
    }

    public Categoty(String id, String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
