package com.example.john.munchies;
//Harvey Cabrias
public class CustomerCartClass {
    private String userEmail, itemName, restaurantName;
    private double price;

    public CustomerCartClass() {
    }

    public CustomerCartClass(String userEmail, String itemName, String restaurantName, double price) {
        this.userEmail = userEmail;
        this.itemName = itemName;
        this.restaurantName = restaurantName;
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
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
