package com.example.john.munchies;
//3/9/2019
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Restaurant extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    ListView restaurantList;
    ArrayList<String> restaurantArrayList;
    ArrayAdapter<String> restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        //Firebase Database
        myFB = FirebaseDatabase.getInstance();


        //View
        restaurantList = (ListView)findViewById(R.id.restaurantListView);

        //
        restaurantArrayList = new ArrayList<String>();
        displayRestaurantList();
        getMenuDetails();

    }

    //Display the restaurant name to the databa
    public void displayRestaurantList(){
//Root munchies, child restaurants
        myRef = myFB.getReference("MunchiesDB").child("Restaurants");

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
        restaurantArrayList.clear();
        for(DataSnapshot item: dataSnapshot.getChildren()){
            RestaurantClass restaurant = item.getValue(RestaurantClass.class);
            restaurantArrayList.add(restaurant.getRestaurantName());
        }


        restaurantAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, restaurantArrayList);
        restaurantList.setAdapter(restaurantAdapter);

    }

    public void getMenuDetails(){
        restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the position
                String restaurantName = String.valueOf(restaurantList.getItemAtPosition(position));
                //Get Placement
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                //Get the string
                editor.putString("RestaurantName", restaurantName);
                //Save
                editor.apply();
                Intent myIntent = new Intent(Restaurant.this, RestaurantMenu.class);
                startActivity(myIntent);
            }
        });
    }

}
