package com.example.john.munchies;

import android.widget.Toast;

public class RestaurantClass {


    private String restaurantID, restaurantName;

    public RestaurantClass() {
    }

    public RestaurantClass(String restaurantID, String restaurantName) {
        this.restaurantID = restaurantID;
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






}
