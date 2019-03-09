package com.example.john.munchies;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Restaurant extends AppCompatActivity {

    DatabaseHelper myDB;
    ListView restaurantList;
    ArrayList<String> restaurantArrayList;
    ArrayAdapter<String> restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        myDB = new DatabaseHelper(this);
        restaurantList = (ListView)findViewById(R.id.restaurantListView);
        restaurantArrayList = new ArrayList<String>();
        displayRestaurantList();

        restaurantAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, restaurantArrayList);
        restaurantList.setAdapter(restaurantAdapter);
    }

    //Display the restaurant name from the database
    public void displayRestaurantList(){
        Cursor cursor = myDB.getRestaurant();
        if(cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "NO DATA", Toast.LENGTH_LONG).show();
            return;
        }
        while(cursor.moveToNext()){
            restaurantArrayList.add(cursor.getString(1).toString());
        }
    }

    //
}
