package com.example.john.munchies;

public class ComplaintClass {
   private String ID, Name, Email, Restaurant, Phone, OrderId, Issue, Subject;

    public ComplaintClass()
    {

    }

    public ComplaintClass(String id, String restaurant, String subject, String phone, String issue) {
        ID = id;
        Restaurant = restaurant;
        Subject = subject;
        Phone = phone;
        Issue = issue;
    }

    public ComplaintClass(String id, String name, String email, String restaurant, String phone, String orderId, String issue) {
        ID = id;
        Name = name;
        Email = email;
        Restaurant = restaurant;
        Phone = phone;
        OrderId = orderId;
        Issue = issue;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }
}
