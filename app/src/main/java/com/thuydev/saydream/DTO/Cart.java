package com.thuydev.saydream.DTO;

public class Cart {
    private String id;
    private String idUser;
    private String idProducts;
    private String size;
    private int quantity;

    public Cart() {
    }

    public Cart(String id, String idUser, String idProducts, String size, int quantity) {
        this.id = id;
        this.idUser = idUser;
        this.idProducts = idProducts;
        this.size = size;
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdProducts() {
        return idProducts;
    }

    public void setIdProducts(String idProducts) {
        this.idProducts = idProducts;
    }
}
