package com.alif.clothingretail.model;

import java.util.List;

public class Request {
    private String phoneNumber;
    private String name;
    private String address;
    private String total;
    private List<Order> clothings; // List of clothing order
    private String status;

    public Request() {
    }

    public Request(String phoneNumber, String name, String address, String total, List<Order> clothings, String status) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.address = address;
        this.total = total;
        this.clothings = clothings;
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getClothings() {
        return clothings;
    }

    public void setClothings(List<Order> clothings) {
        this.clothings = clothings;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
