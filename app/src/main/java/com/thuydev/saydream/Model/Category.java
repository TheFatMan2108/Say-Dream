package com.thuydev.saydream.Model;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {
    private String nameCategory;
    private List<Shoe> shoes;

    public Category(String nameCategory, List<Shoe> shoes) {
        this.nameCategory = nameCategory;
        this.shoes = shoes;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public List<Shoe> getShoes() {
        return shoes;
    }

    public void setShoes(List<Shoe> shoes) {
        this.shoes = shoes;
    }
}
