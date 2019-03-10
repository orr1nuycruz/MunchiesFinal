package com.example.john.munchies;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantMenu extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    String restaurantName;

    ListView restaurantMenuList;
    ArrayList<String> restaurantMenuArrayList;
    ArrayAdapter<String> restaurantMenuAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        restaurantName = sharedPref.getString("RestaurantName", "");

        myFB = FirebaseDatabase.getInstance();
        myRef = myFB.getReference("MunchiesDB");

        restaurantMenuList = (ListView)findViewById(R.id.restaurantMenuListView);
        restaurantMenuArrayList = new ArrayList<String>();

        displayRestaurantList();
    }

    //Display the restaurant name from the database
    public void displayRestaurantList(){
        myRef = myFB.getReference("MunchiesDB").child("RestaurantItems").child(restaurantName);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showData(DataSnapshot dataSnapshot){
        restaurantMenuArrayList.clear();
        for(DataSnapshot item: dataSnapshot.getChildren()){
            RestaurantItemClass restaurant = item.getValue(RestaurantItemClass .class);
            restaurantMenuArrayList.add(restaurant.getItemName() + " " + restaurant.getPrice());
        }


        restaurantMenuAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, restaurantMenuArrayList);
        restaurantMenuList.setAdapter(restaurantMenuAdapter);

    }


}
