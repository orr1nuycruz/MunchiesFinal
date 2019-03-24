package com.example.john.munchies;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RestaurantViewOrders extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> restaurantOrdersArray;
    ArrayAdapter<String> restaurantOrdersAdapter;
    ListView restaurantOrdersListView;
    RestaurantOrderClass restaurantOrderClass;
    String currentDay;
    SimpleDateFormat day;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String restaurantName;
    String val;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view_orders);
        //Shared Instance
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        restaurantName = preferences.getString("RestName", "");

        day = new SimpleDateFormat("yyyy-MM-dd");
        currentDay = day.format(new Date());
        editor = preferences.edit();
        editor.putString("restaurantDay", currentDay);
        editor.commit();
      //  currentDay = preferences.getString("currentDay", "");




        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("MunchiesDB").child("RestaurantOrders").
                child(restaurantName).child("Purchases").child(currentDay);
        //View
        restaurantOrdersListView = (ListView) findViewById(R.id.restaurantOrdersListView);
        restaurantOrdersArray = new ArrayList<String>();

        //Listeners
        getMenuDetails();


        //Other
        displayRestaurantOrders();
    }

    public void getMenuDetails(){




        restaurantOrdersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the position
                String pos = String.valueOf(restaurantOrdersListView.getItemAtPosition(position));
                //Get Placement
                Toast.makeText(getApplicationContext(), pos, Toast.LENGTH_SHORT).show();
                //Get the string
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("pos", pos);


                //Save
                editor.apply();
                Intent myIntent = new Intent(RestaurantViewOrders.this, RestaurantViewCustomerOrders.class);
                startActivity(myIntent);
            }
        });
    }


    //Gets FireBase Data
    public void displayRestaurantOrders(){
//reference = reference.child(currentDay);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Shos data
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

//Show Order
    public void showData(DataSnapshot dataSnapshot){
        restaurantOrderClass = new RestaurantOrderClass();

        for (DataSnapshot item: dataSnapshot.getChildren()){

            //Add it to array
           // restaurantOrderClass.setOrder(item.getKey());
            // Toast.makeText(this, item.getKey() + restaurantOrderClass.getOrder(), Toast.LENGTH_SHORT).show();
            restaurantOrdersArray.add(item.getKey());
        }

        restaurantOrdersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restaurantOrdersArray);
        restaurantOrdersListView.setAdapter(restaurantOrdersAdapter);

    }



    @Override
    public void onClick(View view) {


    }
}
