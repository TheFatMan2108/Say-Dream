package com.thuydev.saydream.DTO;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    private String id;
    private String idUser;
    private String idStaff;
    private ArrayList<Cart> listSP;
    private String date;

    private int status;
    private Long totalPrice;
    public Bill() {
    }

    public Bill(String id, String idUser, String idStaff, ArrayList<Cart> listSP, String date, int status, Long totalPrice) {
        this.id = id;
        this.idUser = idUser;
        this.idStaff = idStaff;
        this.listSP = listSP;
        this.date = date;
        this.status = status;
        this.totalPrice = totalPrice;
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

    public ArrayList<Cart> getListSP() {
        return listSP;
    }

    public void setListSP(ArrayList<Cart> listSP) {
        this.listSP = listSP;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
