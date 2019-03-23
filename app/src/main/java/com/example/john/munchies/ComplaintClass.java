package com.example.john.munchies;

public class ComplaintClass {
   private String Name, Email, Restaurant, Phone, OrderId, Issue;

    public ComplaintClass()
    {

    }

    public ComplaintClass(String name, String email, String restaurant, String phone, String orderId, String issue) {
        Name = name;
        Email = email;
        Restaurant = restaurant;
        Phone = phone;
        OrderId = orderId;
        Issue = issue;
    }

    public ComplaintClass(String restaurant, String email, String phone, String issue) {
        Restaurant = restaurant;
        Email = email;
        Phone = phone;
        Issue = issue;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getRestaurant() {
        return Restaurant;
    }

    public void setRestaurant(String restaurant) {
        Restaurant = restaurant;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIssue() {
        return Issue;
    }

    public void setIssue(String issue) {
        Issue = issue;
    }
}
