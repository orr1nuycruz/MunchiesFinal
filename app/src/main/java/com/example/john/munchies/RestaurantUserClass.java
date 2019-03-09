package com.example.john.munchies;

public class RestaurantUserClass {
    private String restaurantName, username, password;

    public RestaurantUserClass() {
    }

    public RestaurantUserClass(String restaurantName, String username, String password) {
        this.restaurantName = restaurantName;
        this.username = username;
        this.password = password;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
