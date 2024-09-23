package com.thuydev.saydream.DTO;

import java.util.List;

public class User {
    private String id;
    private String email;
    private String numberPhone;
    private String fullName;
    private String image;
    private int status;
    private long balance;
    private List<String> locations;
    private String address;
    public User() {
    }

    public User(String id, String email, String numberPhone, String fullName, String image, int status, long balance) {
        this.id = id;
        this.email = email;
        this.numberPhone = numberPhone;
        this.fullName = fullName;
        this.image = image;
        this.status = status;
        this.balance = balance;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }



}
