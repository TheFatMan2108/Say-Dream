package com.thuydev.saydream.DTO;

public class Bill {
    private String id;
    private String idUser;
    private String idStaff;
    private String[] idProducts;
    private String time;
    private int quantity;
    private int status;

    public Bill() {
    }

    public Bill(String id, String idUser, String idStaff, String[] idProducts, String time, int quantity, int status) {
        this.id = id;
        this.idUser = idUser;
        this.idStaff = idStaff;
        this.idProducts = idProducts;
        this.time = time;
        this.quantity = quantity;
        this.status = status;
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

    public String getIdStaff() {
        return idStaff;
    }

    public void setIdStaff(String idStaff) {
        this.idStaff = idStaff;
    }

    public String[] getIdProducts() {
        return idProducts;
    }

    public void setIdProducts(String[] idProducts) {
        this.idProducts = idProducts;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
