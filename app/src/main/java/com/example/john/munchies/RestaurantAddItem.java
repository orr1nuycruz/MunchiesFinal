package com.example.john.munchies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RestaurantAddItem extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    String restaurantUser;
    String restaurantName;


    EditText editRestaurantMenuName;
    EditText editRestaurantMenuPrice;

    Button btnAddMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_add_item);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        restaurantUser = sharedPref.getString("User", "");
        restaurantName = sharedPref.getString("RestName", "");

        editRestaurantMenuName = (EditText)findViewById(R.id.editAddMenuName);
        editRestaurantMenuPrice = (EditText)findViewById(R.id.editAddMenuPrice);

        myFB = FirebaseDatabase.getInstance();
        myRef = myFB.getReference("MunchiesDB").child("RestaurantItems");

        btnAddMenu = (Button)findViewById(R.id.restaurantAddMenu);
        addMenu();
    }

    public void addMenu(){
        btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getRestaurantItemName = editRestaurantMenuName.getText().toString();
                String getRestaurantUser = restaurantUser.toString();
                String getRestaurantName = restaurantName.toString();
                String getPrice = editRestaurantMenuPrice.getText().toString();
                Double price = Double.valueOf(getPrice).doubleValue();

                if(getRestaurantItemName.equals("") || getPrice.equals("")){
                    Toast.makeText( RestaurantAddItem.this, "Please fill out all information", Toast.LENGTH_LONG).show();
                } else {
                    RestaurantItemClass menu = new RestaurantItemClass( getRestaurantItemName, getRestaurantName, price);
                    myRef.child(getRestaurantName).child(getRestaurantItemName).setValue(menu);
                    editRestaurantMenuName.setText("");
                    editRestaurantMenuPrice.setText("");
                }
            }
        });
    }
}
