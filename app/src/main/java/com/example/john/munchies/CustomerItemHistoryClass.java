package com.example.john.munchies;

public class CustomerItemHistoryClass {
    private String userEmail, itemName, RestaurantName;
    private double price;

    public CustomerItemHistoryClass() {

    }

    public CustomerItemHistoryClass(String userEmail, String itemName, String restaurantName, double price) {
        this.userEmail = userEmail;
        this.itemName = itemName;
        RestaurantName = restaurantName;
        this.price = price;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
