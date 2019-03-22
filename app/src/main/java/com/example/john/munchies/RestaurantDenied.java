package com.example.john.munchies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RestaurantDenied extends AppCompatActivity   implements  View.OnClickListener {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, reference1;
    Button denied_btn, viewOrder_btn;
    String currentDay;
    String orderNum;
    String restaurantName;
    String authUser;

    SharedPreferences.Editor editor;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_denied);

        //Shared Instance
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        restaurantName = preferences.getString("RestName", "");
        currentDay = preferences.getString("currentDay", "");
        orderNum = preferences.getString("pos", "");
        authUser = preferences.getString("authUser", "");

        //View
        denied_btn = (Button) findViewById(R.id.denied_btn);
        viewOrder_btn = (Button) findViewById(R.id.viewOrder_btn);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("MunchiesDB").child("RestaurantOrders").
                child(restaurantName).child("Purchases").child(currentDay).child(orderNum).child(authUser).child("Order").child("Approval");


        //Listeners
        denied_btn.setOnClickListener(this);
        viewOrder_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == denied_btn ) {

            reference.setValue("Denied");
        }
        if (view == viewOrder_btn){
            viewOrder();
        }
}

    public void viewOrder(){
        Intent myIntent = new Intent(this, RestaurantViewCustomerOrders.class);
        startActivity(myIntent);
    }
}
