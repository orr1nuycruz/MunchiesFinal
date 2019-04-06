package com.example.john.munchies;

public class CustomerPaymentClass {




    private String user;
    private String customerName;
    private String orderList;
    private String orderTotal;

    public CustomerPaymentClass() {

    }

    public CustomerPaymentClass(String user, String customerName, String orderList, String orderTotal) {
        this.user = user;
        this.customerName = customerName;
        this.orderList = orderList;
        this.orderTotal = orderTotal;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderList() {
        return orderList;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList;
    }
    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }




}