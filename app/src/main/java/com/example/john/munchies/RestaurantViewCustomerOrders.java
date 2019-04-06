package com.example.john.munchies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.text.SimpleDateFormat;

public class RestaurantViewCustomerOrders extends AppCompatActivity implements View.OnClickListener {
    RestaurantOrderClass customerOrder;

    String currentDay;
    String orderNum;
    String restaurantName;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, reference1;


    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    TextView authUser,order, price, hourCreated, showtimer;
    Button accept_btn, decline_btn, orders_btn;

    Date initial = null,
            current = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view_customer_order);

        //Shared Instance
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
         restaurantName = preferences.getString("RestName", "");
        currentDay = preferences.getString("restaurantDay", "");
        orderNum = preferences.getString("pos", "");
        customerOrder = new RestaurantOrderClass();

        //View
        authUser = (TextView) findViewById(R.id.authUser);
        order = (TextView) findViewById(R.id.order);
        price = (TextView) findViewById(R.id.price);
        hourCreated = (TextView) findViewById(R.id.hourCreated);
        showtimer = (TextView) findViewById(R.id.showtimer) ;

        accept_btn = (Button) findViewById(R.id.accept_btn);
        decline_btn = (Button) findViewById(R.id.decline_btn);
        orders_btn = (Button) findViewById(R.id.orders_btn);


   //     Toast.makeText(this, currentDay + restaurantName + orderNum, Toast.LENGTH_SHORT).show();

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("MunchiesDB").child("RestaurantOrders").
                    child(restaurantName).child("Purchases").child(currentDay).child(orderNum);
        reference1 = firebaseDatabase.getReference("MunchiesDB").child("RestaurantOrders").
                child(restaurantName).child("Purchases").child(currentDay).child(orderNum);
        //Listeners

        //Other
        displayTxtData();
       // displayInnerData();
        accept_btn.setOnClickListener(this);
        decline_btn.setOnClickListener(this);
        orders_btn.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
    if (view == accept_btn ) {
accept();
    }
    if (view == decline_btn){
        decline();
    }

        if (view == orders_btn){
            orders();
        }


    }

    public void orders() {
        Intent myIntent = new Intent(this, RestaurantViewOrders.class);
        startActivity(myIntent);
    }


    public void decline(){
        editor.putString("authUser",authUser.getText().toString());
        editor.commit();

        Intent myIntent = new Intent(this, RestaurantDenied.class);
        startActivity(myIntent);


        //  Toast.makeText(getApplicationContext(), authUser.getText(), Toast.LENGTH_SHORT).show(); retrieved

    }


    public void accept(){
        editor.putString("authUser",authUser.getText().toString());
        editor.commit();

        Intent myIntent = new Intent(this, RestaurantSuccess.class);
        startActivity(myIntent);


      //  Toast.makeText(getApplicationContext(), authUser.getText(), Toast.LENGTH_SHORT).show(); retrieved

    }


////GET KEY

    public void showData(DataSnapshot dataSnapshot){
        for(DataSnapshot item: dataSnapshot.getChildren()){
            //GET CHILD (ONLY 1)
            authUser.setText(item.getKey());

        }
     //   Toast.makeText(getApplicationContext(), user.getAuthUser(), Toast.LENGTH_SHORT).show();

    }

    public void displayTxtData(){

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    //GET CHILD (ONLY 1)
                    authUser.setText(item.getKey());

                }
            //    Toast.makeText(getApplicationContext(), user.getAuthUser() + " or " + authUser.getText(), Toast.LENGTH_SHORT).show();
                reference.child(authUser.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int n = 0;
                        String orderItem = "";
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            //GET CHILD (ONLY 1)

                            customerOrder = item.getValue(RestaurantOrderClass.class);
                            price.setText(customerOrder.getPrice());
                            while (n < customerOrder.getItems().size()) {
                                orderItem += customerOrder.getItems().get(n);
                                n = n + 1;

                            }
                            order.setText(orderItem);

                            //TIMER
                            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                            hourCreated.setText(customerOrder.getHourCreated());
                            String todayTime = dateFormat.format(new Date());
                            try{
                                initial = dateFormat.parse(customerOrder.getHourCreated());
                                current = dateFormat.parse(todayTime);
                            }
                            catch(Exception e){

                            }
                            long diff = current.getTime() - initial.getTime();
                            long diffMinutes = diff / (60 * 1000);
                            showtimer.setText(diffMinutes + " minute since order");
                            if(diffMinutes > 1){
                                showtimer.setText(diffMinutes + " minutes since order");
                            }
                            if(diffMinutes > 2){
                                showtimer.setText(diffMinutes + " minutes since order is made. Since it's over 2 minutes, order can no longer be accepted");
                                accept_btn.setEnabled(false);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                }

                );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
