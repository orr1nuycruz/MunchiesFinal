package com.example.john.munchies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RestaurantSuccess extends AppCompatActivity implements  View.OnClickListener{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, reference2;
    Button notify_btn, viewOrder_btn;;
    String currentDay;
    String orderNum;
    String restaurantName;
    String authUser;
    String kept;

    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_success);

        //Shared Instance
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        restaurantName = preferences.getString("RestName", "");
        currentDay = preferences.getString("currentDay", "");
        orderNum = preferences.getString("pos", "");
        authUser = preferences.getString("authUser", "");
        String[] split = authUser.split(" ");
        kept = split[1];

        //View
        notify_btn = (Button) findViewById(R.id.approved_btn);
        viewOrder_btn = (Button) findViewById(R.id.viewOrder_btn);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("MunchiesDB").child("RestaurantOrders").
                child(restaurantName).child("Purchases").child(currentDay).child(orderNum).child(authUser).child("Order").child("Approval");

        reference2 = firebaseDatabase.getReference("MunchiesDB").child("OrderHistory").child(kept).child(orderNum).child("approval");



        //Listeners
        notify_btn.setOnClickListener(this);
        viewOrder_btn.setOnClickListener(this);

}

    @Override
    public void onClick(View view) {
        if (view == notify_btn ) {
            reference.setValue("Accepted");
            reference2.setValue("Accepted");
        }

        if (view == viewOrder_btn){
            viewOrder();
        }

    }

    public void viewOrder(){
        Intent myIntent = new Intent(this, RestaurantViewCustomerOrders.class);
        startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // moveTaskToBack(true);

    }
}
