package com.thuydev.saydream.DTO;

public class Product {
    private String id;
    private String idCategory;
    private String name;
    private long price;
    private String[] size;
    private String year_of_manufacture;
    private int quantity;

    public Product() {
    }

    public Product(String id, String idCategory, String name, long price, String[] size, String year_of_manufacture, int quantity) {
        this.id = id;
        this.idCategory = idCategory;
        this.name = name;
        this.price = price;
        this.size = size;
        this.year_of_manufacture = year_of_manufacture;
        this.quantity = quantity;
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

    public String[] getSize() {
        return size;
    }

    public void setSize(String[] size) {
        this.size = size;
    }

    public String getYear_of_manufacture() {
        return year_of_manufacture;
    }

    public void setYear_of_manufacture(String year_of_manufacture) {
        this.year_of_manufacture = year_of_manufacture;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
