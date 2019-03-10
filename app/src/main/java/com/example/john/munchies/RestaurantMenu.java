package com.example.john.munchies;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.widget.AbsListView.CHOICE_MODE_SINGLE;

public class RestaurantMenu extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    String restaurantName;
    String userEmail;

    ListView restaurantMenuList;
    ArrayList<String> restaurantMenuArrayList;
    ArrayAdapter<String> restaurantMenuAdapter;

    Button btnAddToMyCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        restaurantName = sharedPref.getString("RestaurantName", "");
        userEmail = sharedPref.getString("userEmail", "");

        myFB = FirebaseDatabase.getInstance();
        myRef = myFB.getReference("MunchiesDB");

        btnAddToMyCart = (Button)findViewById(R.id.btnAddToMyCart);

        restaurantMenuList = (ListView)findViewById(R.id.restaurantMenuListView);
        restaurantMenuArrayList = new ArrayList<String>();

        displayRestaurantList();
//        addMenuToMyCart();
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

    public void addMenuToMyCart(){
        btnAddToMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        RestaurantItemClass item = new RestaurantItemClass();
                        String getUserEmail = userEmail;
                        String getRestaurant = restaurantName;
                        String getItemName = item.getItemName();
                        Double Price = Double.valueOf(item.getPrice()).doubleValue();
                        String toast = String.valueOf(restaurantMenuList.getItemAtPosition(position));
                        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
                        if(getItemName.equals("")){
                            Toast.makeText( RestaurantMenu.this, "Please fill out all information", Toast.LENGTH_LONG).show();
                        } else {
                            CustomerCartClass menu = new CustomerCartClass( getUserEmail, getRestaurant, getItemName, Price);
                            myRef.child(getUserEmail).setValue(menu);
                        }
                    }
                });


            }
        });

    }


}
