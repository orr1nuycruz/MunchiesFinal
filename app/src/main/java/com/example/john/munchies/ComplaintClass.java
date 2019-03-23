package com.example.john.munchies;

public class ComplaintClass {
   private String Name, Phone, OrderId, Issue;

    public ComplaintClass()
    {

    }

    public ComplaintClass(String id, String name, String phone, String issue)
    {
        OrderId = id;
        Name = name;
        Phone = phone;
        Issue = issue;

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
