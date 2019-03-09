package com.example.john.munchies;

import android.widget.Toast;

public class ObjectRestaurant {




    private String restaurantID;
    private String restaurantName;
    private String menuItem;
    private int price;

    public ObjectRestaurant(){
    }

    public ObjectRestaurant(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
