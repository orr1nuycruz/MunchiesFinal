package com.example.john.munchies;

public class RestaurantItemClass {
    private String itemID, itemName, restaurantName;
    private double price;

    public RestaurantItemClass() {

    }

    public RestaurantItemClass(String itemID, String itemName, String restaurantName, double price) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.restaurantName = restaurantName;
        this.price = price;
    }

    public RestaurantItemClass(String itemName, String restaurantName, double price){
        this.itemName = itemName;
        this.restaurantName = restaurantName;
        this.price = price;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
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
