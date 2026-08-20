package com.kodbank;

import java.time.LocalDateTime;

/**
 * Represents a bank user/customer
 */
public class User {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private boolean isActive;

    public User(String userId, String name, String email, String phone, String address) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public void updateProfile(String name, String email, String phone, String address) {
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (phone != null) this.phone = phone;
        if (address != null) this.address = address;
    }

    @Override
    public String toString() {
        return "User(" + userId + ", " + name + ", " + email + ")";
    }
}
