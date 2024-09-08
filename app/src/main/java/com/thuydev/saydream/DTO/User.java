package com.thuydev.saydream.DTO;

public class User {
    private String id;
    private String email;
    private String fullName;
    private int status;
    private long balance;

    public User() {
    }

    public User(String id, String email, String fullName, int status, long balance) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.status = status;
        this.balance = balance;
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", status=" + status +
                ", balance=" + balance +
                '}';
    }
}
