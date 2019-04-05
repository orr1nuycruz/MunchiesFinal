package com.example.john.munchies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    public void goToLogin(View v){
        Intent i = new Intent(Homepage.this, Login.class);
        startActivity(i);
    }

    public void goToResLogin(View v) {
        Intent i = new Intent(Homepage.this, RestaurantLogin.class);
        startActivity(i);
    }


    public void goToLAdd(View v){
        Intent i = new Intent(Homepage.this, AddFireBaseActivity.class);
        startActivity(i);
    }

    public void goToShow(View v){
        Intent i = new Intent(Homepage.this, ShowFirebaseActivity.class);
        startActivity(i);
    }

    public void goToAddRestaurant(View v){
        Intent i = new Intent(Homepage.this, AdminAddRestaurant.class);
        startActivity(i);
    }
    public void goToComplaint(View v) {
        Intent i = new Intent(Homepage.this, CustomerComplaint.class);
        startActivity(i);
    }

    public void goToResComplaint(View v) {
        Intent i = new Intent(Homepage.this, RestauantComplaint.class);
        startActivity(i);
    }

    public void goToComplaintList(View v) {
        Intent i = new Intent(Homepage.this, ComplaintList.class);
        startActivity(i);
    }

}
