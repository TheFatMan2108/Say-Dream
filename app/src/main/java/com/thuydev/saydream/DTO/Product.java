package com.thuydev.saydream.DTO;

import java.util.List;

public class Product {
    private String id;
    private String idCategory;
    private String name;
    private String image;
    private long price;
    private List<String> size;
    private String yearOfManufacture;
    private int quantity;

    public Product() {
    }

    public Product(String id, String idCategory, String name, String image, long price, List<String> size, String yearOfManufacture, int quantity) {
        this.id = id;
        this.idCategory = idCategory;
        this.name = name;
        this.image = image;
        this.price = price;
        this.size = size;
        this.yearOfManufacture = yearOfManufacture;
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public String getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(String year_of_manufacture) {
        this.yearOfManufacture = year_of_manufacture;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
