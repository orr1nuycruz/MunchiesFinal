package com.example.john.munchies;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantMenuCRUD extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    Button btnAddMenu;
    Button btnUpdateMenu;
    Button btnDeleteMenu;

    EditText editRestaurantMenuName;
    EditText editRestaurantMenuPrice;

    String restaurantUser;
    String restaurantName;


    ListView restaurantMenuList;
    ArrayList<String> restaurantMenuArrayList;
    ArrayAdapter<String> restaurantMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu_crud);

        myFB = FirebaseDatabase.getInstance();
        myRef = myFB.getReference("MunchiesDB").child("RestaurantItems");

        btnAddMenu = (Button)findViewById(R.id.btnAddMenu);
        btnUpdateMenu = (Button)findViewById(R.id.btnUpdateMenu);
        btnDeleteMenu = (Button)findViewById(R.id.btnDeleteMenu);
        btnUpdateMenu.setEnabled(false);
        btnDeleteMenu.setEnabled(false);

        editRestaurantMenuName = (EditText)findViewById(R.id.EditMenuName);
        editRestaurantMenuPrice = (EditText)findViewById(R.id.EditMenuPrice);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        restaurantUser = sharedPref.getString("User", "");
        restaurantName = sharedPref.getString("RestName", "");


        restaurantMenuList = (ListView)findViewById(R.id.listMenuItemCRUD);
        restaurantMenuArrayList = new ArrayList<String>();

        displayRestaurantList();
        addMenu();
        deleteMenuItem();
        updateMenuItem();
    }

    @Override
    public void onBackPressed(){
        finish();
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
                    Toast.makeText( RestaurantMenuCRUD.this, "Please fill out all information", Toast.LENGTH_LONG).show();
                } else {
                    RestaurantItemClass menu = new RestaurantItemClass( getRestaurantItemName, getRestaurantName, price);
                    myRef.child(getRestaurantItemName).setValue(menu);
                    editRestaurantMenuName.setText("");
                    editRestaurantMenuPrice.setText("");
                }
            }
        });
    }

    public void updateMenuItem(){
        btnUpdateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET position of the selected item
                String getRestaurantItemName = editRestaurantMenuName.getText().toString();
                String getRestaurantPrice = editRestaurantMenuPrice.getText().toString();
                String getRestaurantName = restaurantName.toString();
                Double price = Double.valueOf(getRestaurantPrice).doubleValue();
                //Enable or Disable Buttons
                btnAddMenu.setEnabled(true);
                btnUpdateMenu.setEnabled(false);
                btnDeleteMenu.setEnabled(false);

                //Enable editing
                editRestaurantMenuName.setEnabled(true);

                editRestaurantMenuName.setText("");
                editRestaurantMenuPrice.setText("");
                if(!getRestaurantItemName.isEmpty() && getRestaurantItemName.length() > 0){
                    RestaurantItemClass menu = new RestaurantItemClass( getRestaurantItemName, getRestaurantName, price);
                    myRef.child(getRestaurantItemName).setValue(menu);
                }

            }
        });
    }


    public void deleteMenuItem(){
        final RestaurantItemClass restaurant = new RestaurantItemClass();
        restaurantMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                restaurant.setItemID(restaurantMenuArrayList.get(position));
                editRestaurantMenuName.setText(restaurantMenuArrayList.get(position));
                //Enable or Disable Buttons
                btnAddMenu.setEnabled(false);
                btnUpdateMenu.setEnabled(true);
                btnDeleteMenu.setEnabled(true);

                //Display edit text
                editRestaurantMenuName.setEnabled(false);
            }
        });

        btnDeleteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = restaurant.getItemID();
                Toast.makeText(getApplicationContext(), "Deleted " +  name  + " item menu", Toast.LENGTH_SHORT).show();
                if(name.equals("")){
                    Toast.makeText( RestaurantMenuCRUD.this, "Please Select item before delete!", Toast.LENGTH_LONG).show();
                } else {
                    myRef.child(restaurantName).child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            myRef.child(name).removeValue();
                            //Set text to empty
                            editRestaurantMenuName.setText("");
                            editRestaurantMenuPrice.setText("");

                            //Enable or Disable Buttons
                            btnAddMenu.setEnabled(true);
                            btnUpdateMenu.setEnabled(false);
                            btnDeleteMenu.setEnabled(false);

                            //Enable editing
                            editRestaurantMenuName.setEnabled(true);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
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
            restaurantMenuArrayList.add(restaurant.getItemName());
        }


        restaurantMenuAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, restaurantMenuArrayList);
        restaurantMenuList.setAdapter(restaurantMenuAdapter);

    }
}
