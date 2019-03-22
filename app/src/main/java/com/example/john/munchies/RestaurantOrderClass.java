package com.example.john.munchies;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RestaurantOrderClass {
//Class must corresponds to NOSQL DBA
   // private HashMap<String, String> Order;
    private String HourCreated;
    private String Approval;
    private ArrayList<String> Items;
    private  String Price;



    RestaurantOrderClass() {

    }
    RestaurantOrderClass( String hourCreated, String approval, ArrayList<String> items, String price) {
    this.HourCreated = hourCreated; this.Approval = approval;  this.Items = items; this.Price = price;
    }

    public String getHourCreated() {
        return HourCreated;
    }

    public String getIsCompleted() {
        return Approval;
    }

    public ArrayList<String> getItems() {
        return Items;
    }

    public String getPrice() {
        return Price;
    }


}