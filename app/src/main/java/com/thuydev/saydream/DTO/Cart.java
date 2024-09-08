package com.thuydev.saydream.DTO;

public class Cart {
    private String id;
    private String idUser;
    private String[] idProducts;

    public Cart() {
    }

    public Cart(String id, String idUser, String[] idProducts) {
        this.id = id;
        this.idUser = idUser;
        this.idProducts = idProducts;
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

    public String[] getIdProducts() {
        return idProducts;
    }

    public void setIdProducts(String[] idProducts) {
        this.idProducts = idProducts;
    }
}
