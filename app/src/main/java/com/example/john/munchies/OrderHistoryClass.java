package com.example.john.munchies;

public class OrderHistoryClass {
    String orderNum, currentDay, price;
    String order, approval;

    public OrderHistoryClass() {

    }


    public OrderHistoryClass(String orderNum, String currentDay, String price, String order, String approval) {
        this.orderNum = orderNum;
        this.currentDay = currentDay;
        this.price = price;
        this.order = order;
        this.approval = approval;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
