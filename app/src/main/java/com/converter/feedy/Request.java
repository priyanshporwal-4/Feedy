package com.converter.feedy;

public class Request {
    private String name;
    private String phone;
    private String quantity;
    private String expiry;
    private String food_items;
    private String latitude;
    private String longitude;

    public Request() {
        //empty cons
    }

    public Request(String name, String phone, String quantity, String expiry, String food_items, String latitude, String longitude) {
        this.name = name;
        this.phone = phone;
        this.quantity = quantity;
        this.expiry = expiry;
        this.food_items = food_items;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getFood_items() {
        return food_items;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
